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
package com.zerocracy.bundles.resigns_performer_when_unassigned_from_github

import com.jcabi.github.Event
import com.jcabi.github.Github
import com.jcabi.github.Issue
import com.jcabi.github.Repos
import com.jcabi.xml.XML
import com.zerocracy.entry.ExtGithub
import com.zerocracy.Farm
import com.zerocracy.Project
import com.zerocracy.farm.fake.FkFarm
import com.zerocracy.pmo.People
import com.zerocracy.radars.github.RbOnUnassign
import javax.json.Json

def exec(Project project, XML xml) {
  Farm farm = binding.variables.farm
  Github github = new ExtGithub(farm).value()
  def repo = github.repos().create(new Repos.RepoCreate('test', false))
  def issue =
    new Issue.Smart(repo.issues().create('Hello, world', ''))
  repo.issueEvents()
    .create(Event.UNASSIGNED, issue.number(), 'yegor256', com.google.common.base.Optional.absent())
  new People(project).bootstrap()
  new RbOnUnassign().react(
    new FkFarm(project),
    github,
    Json.createObjectBuilder()
      .add('issue', Json.createObjectBuilder().add('number', issue.number()))
      .add('repository', Json.createObjectBuilder()
      .add('full_name', repo.coordinates().toString()))
      .add('assignee', Json.createObjectBuilder().add('login', 'yegor256'))
      .build()
  )
}
