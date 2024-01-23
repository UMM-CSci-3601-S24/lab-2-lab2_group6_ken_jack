# Lab Tasks <!-- omit in toc -->

- [Notes on notation and structure](#notes-on-notation-and-structure)
- [Exploring the project](#exploring-the-project)
- [Exploring the server](#exploring-the-server)
  - [Use Thunder Client to explore API output](#use-thunder-client-to-explore-api-output)
- [Exploring the client](#exploring-the-client)
- [Use GitHub Projects to support Agile development](#use-github-projects-to-support-agile-development)
  - [Setting up the GitHub Projects board](#setting-up-the-github-projects-board)
  - [Using the board](#using-the-board)
- [The epics/features](#the-epicsfeatures)
- [Questions](#questions)

## Notes on notation and structure

* Questions that you need to answer (as a group!) are indicated with question mark symbols (:question:).
* The [Questions](./LABTASKS.md#questions) section is at the end of this document.
* Tasks that specify work to do without a written response will be bulleted.

Responses to questions should be submitted as specified below (in the [QUESTIONS](./LABTASKS.md#questions)).

If you're ever confused about what you need to do for a given task, ask.
Similarly, if you're just not sure what's going on or what something does, or
how it does it, please ask! There's a _lot_ going on here, and if you're not
confused now and then you're probably not paying attention. :smile:

Before completing these lab tasks, make sure you have read through [`README.md`](./README.md) and completed the following:

* Set up your project [`README.md`](./README.md#setup)
* Run your server: [run configuration](./README.md#running-your-project)
* Run your server tests: [testing your server](./README.md#testing-your-project)

## Exploring the project

Look over the directory structure of the project before you start
making changes to it, and consider the various tools that we are
using to manage our project.

:question: Answer questions *1*, *2*, and *3* [QUESTIONS](./LABTASKS.md#questions)

## Exploring the server

Study the server (Java) code in the project you have cloned.
Run it according to the instructions in the
[README](./README.md), including running the JUnit tests. Answer
the following questions.

> Pro-tip: Using Google to gain additional knowledge or support your conjectures
about how something works is great! It's important that you think about how everything
fits together and works, though, so don't use Google as a replacement for
building your understanding or you will regret it!

:question: Answer questions *4* and *5* [QUESTIONS](./LABTASKS.md#questions)

Look at the tests in `server/src/test/java/umm3601.user` as they can
provide useful information about the intention of various
functions called by `Server` via the `UserController` class.

You should make sure you run the JUnit tests, and it would be
good to deliberately modify some of the tests and see what
happens when they break. (But make sure you restore them to
their passing state when you're done.)

:warning: We provide very solid testing for everything except for
the top level `Server` class, which is at the moment completely
untested. We think we could use
[functional testing in Javalin](https://javalin.io/tutorials/testing)
to provide coverage of the `Server` class, but we haven't tried that
yet. (You can, though. 😄)

### Use Thunder Client to explore API output

Thunder Client is a tool for debugging the server API output from VSCode. 
It aids in checking what the server gives us when we make requests to it, which can be 
really helpful when you're trying to debug what your server gives you.


To use Thunder Client (once it's installed), open it from the sidebar. 
The icon is a circle with a lightning bolt in the middle.

<img src = "https://user-images.githubusercontent.com/32685970/214179360-2ab176da-dc4f-43f8-8519-4ade1660ef89.png" height = 300 />

This should add a button in the top of the sidebar labelled `New Request`, click it.

![Thunder client startup screen](https://user-images.githubusercontent.com/32685970/214179462-d89c738c-7ab3-4ede-99a8-a3c240169884.png)

This should open a window with two columns. In the top of the left column,
there should be a URL bar with a url, (by default, it's `https://www.thunderclient.com/welcome`). 
Change that to `http://localhost:4567/api/<the-route-you-want-to-test>` (ie. `http://localhost:4567/api/users`), then press send.

![Thunder client usage](https://user-images.githubusercontent.com/32685970/214179602-528f347b-b825-4446-9c91-d6671d8ad0bb.png)

The response will be on the right column. You can also change the query parameters from this window.

## Exploring the client

The client resources are in `client`, which
contains the necessary HTML, CSS, and JavaScript files to
construct the _very_ simple client-side web app.

:question: Answer questions *6* and *7* and *8* [QUESTIONS](./LABTASKS.md#questions)

## Use GitHub Projects to support Agile development

We'll be using GitHub Projects to augment the standard GitHub issues
system with nifty powers to aid in Agile estimating,
planning, tracking, and development. The next two sections
describe the software development tasks you need to complete
for this lab, which all take the form of augmenting the server API with new functionality.

### Setting up the GitHub Projects board

Before you actually start _coding_ on any part of the lab, you
should spend some time using issues and GitHub Projects to capture and estimate
issues and do some planning.

1. Go to the `Issues` tab for your repository
2. Near the green `New issue` button, there is a button-like thing that says `Milestones` (click it)
3.  Click the green `New milestone` button
4.  Create a milestone for the lab that uses the lab's due date
    1.  If you'd like to make multiple, smaller milestones, you may do so
    2.  You can write in other information if you'd like, but at least include the one milestone for the lab's due date

Once you have created a milestone, you will be ready to create a GitHub Projects board to act as your visual workspace that is connected to your GitHub repository.


>In future labs and the project, you'll need to create several epics, one for each major feature; implementing most epics will have at least two parts that together "slice the cake":
>
>* Implementing the server-side functionality, e.g., adding support for a new API endpoint
     to the Javalin server code.
>* Adding the client-side functionality that allows users to access that new server-side work, e.g., 
     adding elements to the website that allow a user to find todos with certain filters activated.
>
>Since we've provided you with a fully functional client, you won't need to do any
work on the client side in this lab, so you won't *really* slice the cake here, but you should
be aware that it will be important in the future for your issues to fully slice the cake. In the future, for each epic you should add the issues (tasks) that you think you'll need to complete to provide a full version of this feature. We will give you instructions about how to do that for future labs.
>
>:warning: One thing you should **not** do is create separate tasks for things like unit tests
or refactoring. Those activities should be "baked in" to your work flow, and not considered
separate (and therefore to some degree optional) activities.

5. Click the `Projects` tab on your GitHub repository
6. Use the green dropdown by the button to make that button read `New project` (then, click `New project`)
7. In the popup dialog, choose the last option `Feature` (has a little wrench by it) and then click the green `Create` button

The view that you see will have several columns: Title, Assignees, Status, Iteration, Estimate, Linked pull requests, and Labels. There is an option to add more columns, and I'd like you to add a column for `Milestone`.

1. I suggest choosing the  `Current iteration` tab and removing the columns for `Ready`. I think that 5 columns is plenty to describe the state of elements of the lab.
2. Go back to the `Home` tab for the project
3.  For each of the features (and additional features) listed below, add an entry to your project as follows:
    1. Click or use control space to start typing the name of the issue/feature
    2. For `Assignees`, you can leave it blank or select your team members
    3. `Status` will tell which column to put this in (I suggest "Backlog")
    4. For `Iteration`, select the current iteration
    5. For `Estimate`, you can leave that blank for now or make an estimate now
    6. There are not linked PRs right now
    7. When you get to the `Labels` column, there is an option to convert the item to an issue. Select your repository and do that conversion.
    8. In the `Milestone` column, select the appropriate milestone (probably the lab's due date, but maybe you added more options)

If you haven't already assigned estimates as you went along, now is a good time to think about how difficult you think each task will be and put estimates on each issue.
Once you've created and estimated all the issues, you
should think about which ones you think you can reasonably
do in this lab. This could be all of them, but it doesn't
have to be. You can always add issues to this epic as
things progress, and in general customers would rather see
the set of issues you expect to complete in this epic
_increase_ rather than _decrease_, so being conservative in
your initial planning is probably a Good Thing.

* You should move the issues you don't expect to do into the `New` track (this is often called `Icebox`, but by default it's called `New` here, so I'm going with that), and move all the other issues into the `Backlog` track. Now, you are ready to get started working on the coding part of this lab!

### Using the board

You'll then need to keep an eye on your board throughout the
lab, using it to guide your decisions about what to work on,
updating issues as you make progress, etc.

Whenever you sit down to work on the project, you should be
clearly working on a specific issue. If you feel like there's
something that _needs_ to be done but isn't in an issue, you
should make an issue for that before you start working on it.

When you start work on a new issue, you should create a
feature branch for that issue, and commit your work on that
issue to that branch. Commit messages should refer to that
issue (by number, e.g., `Issue #8`) so GitHub can auto-link
the commits to that issue for you.

When you feel like an issue is complete

* Move that card to the `In review` track
* Issue a Pull Request from your feature branch onto your master branch

Then step away from that issue for a while,
either by working on a different part of the lab, or by
doing something unrelated to Software Design. Then come back
back to that _as a team_ and review the requirements
described in the issue and compare them to the functionality
you implemented. Is the issue _done done_? Are there solid
and complete tests that back up the work? Can you break it?
Have you tried? Would you bet your career (or at least your
next raise) on this working in a customer demo or out in the
field?

If you find bugs, document them, either in the existing issue, or through new issues. Then go back to working in
the feature branch for that issue, and repeat the whole
process.

Once the issue passes review, you should

* Merge the associated feature branch into master by accepting the (perhaps modified) pull request
* Move the issue to the `Done` track (or, fee free to create more tracks as you see fit)
* There are ways to automate the moves through the tracks based on what's happening in GitHub, but we won't look at that in detail for this lab.

## The epics/features

The initial server (Java) code demonstrates reading in a
collection of (randomly generated) user data, and making it
available (with filtering) via the simple API you explored above.
The client (JavaScript/HTML/CSS) demonstrates using simple forms
that allow the user to make requests of the server and see the results.

Your goal in this lab is to use test-driven development (TDD) to
extend the server's API to support serving 'to-do' data in such a way that it works with the provided client.
We **do** want you to write JUnit tests for the server functionality you
add, but you don't need to worry about the JavaScript code or how one
might test it. (We'll end up using some nice testing
tools that integrate with Angular.)

There is a file `data/todos.json` that has several hundred randomly
generated "to-do"s, each of which has:

* A unique `_id`
* An `owner`
* A `status` (which is a boolean - is the task completed or not)
* A `body` that describes the task
* A `category`

Below are the various features we'd like to see you implement in this lab. You should
create an epic for each of the features listed below, adding at issues as appropriate.

At the very least (necessary to get 85% of this part of the lab)
you should implement (and create meaningful server-side tests for) the following features:

* List all the todos
  * Implement an `api/todos` server-side endpoint, which returns all the to-dos
* List a single todo by ID
  * Implement an `api/todos/58895985c1849992336c219b` server-side endpoint, which
    returns the single todo with the given `_id`. It should return a 404
    (use the Javalin `NotFoundResponse` class) if there is no todo with the
    specified `_id`.
* Support limiting the number of todos that are displayed
  * Implement an `api/todos?limit=7` API endpoint, which lets you specify the maximum
    number of todos that the server returns.
* Support filtering todos by their status (either complete or incomplete)
  * Implement an `api/todos?status=complete` (or `incomplete`) endpoint which lets you
    filter the todos and only return the complete (or incomplete) ones
  * Note that the "database" stores the status as a boolean, but the endpoint uses
    "complete" and "incomplete". You'll have to implement the (simple) logic that
    transforms the endpoint "language" into the database terminology.
* Support searching for todos whose _bodies_ contain a given string
  * Implement an `api/todos?contains=banana` endpoint which lets you search for to-dos
    whose _bodies_ contain (anywhere) the given string (in this case "banana").

To get full (100%) credit on this part of the lab you should
implement (and create meaningful tests for) these additional features:

* Filter todos by owner
  * Implement the endpoint `api/todos?owner=Blanche` which returns just the to-dos
owned by Blanche
* Filter todos by category
  * Implement the endpoint `api/todos?category=groceries` which returns just the to-dos
in the `groceries` category
* Allow for ordering/sorting of todos by a particular attribute
  * Implement the endpoint `api/todos?orderBy=owner` (or `body`, `status`, or `category`)
    which sorts the returned to-dos alphabetically by the specified field

For full credit you also need to support arbitrary combinations
of these filters, e.g.,

```http
api/todos?owner=Blanche&status=complete&limit=12&orderBy=category
```

which would return the first 12 completed to-dos owned by
Blanche ordered by category. Make sure you do the limiting step last so you don't miss any items.

Each of these if implemented properly should work in the provided client.

---

## Questions

Write up your answers to these questions in a Google Doc and turn that in via
Canvas on the assignment for this lab.

:bangbang:

* [ ] __Make sure that everyone in your group has edit privileges on the document.__
* [ ] __Make sure that the link you turn in gives us at least comment privileges.__
* [ ] __Include the URL of the GitHub repository for your group at the top of the
   GDoc. This will make it easier for us to figure out which team is "Snoozing Llamas".__
  
:bangbang: Make sure that your answers address the _purpose_ of
these tools. Don't just tell us _what_ something does, indicate
_why_ we'd want to have it.

:question: *1* What is the purpose of `.gitignore`?
([Maybe search for `.gitignore`?](https://www.google.com/search?q=.gitignore))

:question: *2* What role is Gradle playing in the
project, and what is the purpose of `build.gradle`?

:question: *3* What is the purpose of Github Actions?

:question: *4* Explain what an _endpoint_ is (also often called a _route_). (You might look at the
[Javalin](https://javalin.io/documentation#endpoint-handlers)
documentation for some help here.)

:question: *5* What is the purpose of `umm3601.Server` class?
What is the purpose of the `umm3601.user.UserController` class?
Explain what happens when a user accesses each of the
following URLs:

* :question: The page `users`
  * <http://localhost:4567/users.html>
* :question: The page `api/users`
  * <http://localhost:4567/api/users>
* :question: The page `api/users?age=25`
  * <http://localhost:4567/api/users?age=25>
* :question: The page `api/users/588935f5de613130e931ffd5`
  * <http://localhost:4567/api/users/588935f5de613130e931ffd5>

:bangbang: If you have your project running (see the README), these links should
actually work and generate results from your server.

:question: *6* What are the contents of the `client` folder? What is the purpose of each of the HTML files there?

:question: *7* Describe what happens when you filter users by
age in the client?

* What information is read from the web page, and where is it read from?
* What request is sent to the server?
* What reply does the server send back to the client? How is that constructed?
* What is received by the client, and how/where is it displayed?

:question: *8* Where is the client-side JavaScript defined? Name the HTML file(s) that
load and use it.
