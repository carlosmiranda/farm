/**
 * Copyright (c) 2016-2018 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.farm.uplinked;

import com.zerocracy.Farm;
import com.zerocracy.Item;
import com.zerocracy.Project;
import com.zerocracy.pmo.Pmo;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.SolidList;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.Or;
import org.cactoos.scalar.Ternary;

/**
 * Project that can fetch files from PMO.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "origin")
final class UplinkedProject implements Project {

    /**
     * Files to fetch from PMO.
     */
    private static final Collection<Pattern> FILES = new HashSet<>(
        new SolidList<>(
            new Mapped<>(
                Pattern::compile,
                new IterableOf<>(
                    "awards/[a-zA-Z0-9-]+\\.xml",
                    "agenda/[a-zA-Z0-9-]+\\.xml",
                    "projects/[a-zA-Z0-9-]+\\.xml",
                    "catalog\\.xml",
                    "people\\.xml"
                )
            )
        )
    );

    /**
     * Origin project.
     */
    private final Project origin;

    /**
     * Farm.
     */
    private final Farm farm;

    /**
     * Ctor.
     * @param pkt Project
     * @param frm Farm
     */
    UplinkedProject(final Project pkt, final Farm frm) {
        this.origin = pkt;
        this.farm = frm;
    }

    @Override
    public String pid() throws IOException {
        return this.origin.pid();
    }

    @Override
    public Item acq(final String file) throws IOException {
        return new IoCheckedScalar<>(
            new Ternary<Item>(
                () -> !"PMO".equals(this.origin.pid()) && new Or(
                    new Mapped<>(
                        pattern -> () -> pattern.matcher(file).matches(),
                        UplinkedProject.FILES
                    )
                ).value(),
                () -> new Pmo(this.farm).acq(file),
                () -> this.origin.acq(file)
            )
        ).value();
    }

}
