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
package com.zerocracy.stk.pm.in.links

import com.jcabi.github.Coordinates
import com.jcabi.github.Github
import com.jcabi.xml.XML
import com.zerocracy.Par
import com.zerocracy.entry.ExtGithub
import com.zerocracy.farm.Assume
import com.zerocracy.Farm
import com.zerocracy.Project
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.ClaimOut

def exec(Project project, XML xml) {
  new Assume(project, xml).notPmo()
  new Assume(project, xml).type('Project link was added')
  ClaimIn claim = new ClaimIn(xml)
  String rel = claim.param('rel')
  String href = claim.param('href')
  if ('github' == rel) {
    Farm farm = binding.variables.farm
    Github github = new ExtGithub(farm).value()
    try {
      github.repos().get(new Coordinates.Simple(href)).stars().star()
    } catch (AssertionError ex) {
      new ClaimOut()
        .type('Notify project')
        .param(
          'message',
          new Par(
            'I failed to add GitHub star to %s,',
            'most likely the repository is either absent or',
            'Zerocrat doesn\'t have proper access: %s'
          ).say(href, ex.message)
        )
        .postTo(project)
    }
  }
}
