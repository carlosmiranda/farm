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
package com.zerocracy;

import java.io.IOException;

/**
 * Soft exception from {@link Stakeholder}.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.11
 */
public final class SoftException extends IOException {

    /**
     * Serialization marker.
     */
    private static final long serialVersionUID = -6427942021962997442L;

    /**
     * Ctor.
     * @param cause Cause of it
     */
    public SoftException(final String cause) {
        super(cause);
    }

    /**
     * Ctor.
     * @param cause Cause of it
     * @param thr Throwable
     */
    public SoftException(final String cause, final Throwable thr) {
        super(cause, thr);
    }

}
