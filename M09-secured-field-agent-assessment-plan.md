
# Secured React Field Agent Assessment

## Tasks

_TODO_ Add time estimates to each of the top-level tasks

### Part 0: Set Up and Planning

* [x] Create a new GitHub repo for this assessment or continue working in the repo from last week's React Field Agent repository (0.25 hours)
  * [x] **When creating your repo, be sure to add a `.gitignore` file using the GitHub Node template**
  *[x] Update the README with the contents from this file
  * [x] If not done, add the instruction team as collaborators

* [x] Create a new branch for all work on the assessment
  * From within the local repo, create the branch by running the command `git checkout -b assessment-work` (this command creates a branch named `assessment-work` and makes the new branch the active branch)
  * Push the branch to the remote repo by running the command `git push --set-upstream origin assessment-work`
  * _Now just stay on the `assessment-work` branch and use the normal git workflow:_
    * Make code changes
    * Run `git status` to review changes
    * Run `git add .` to stage changes
    * Run `git commit -m "Some commit message"` to commit changes
    * Run `git push` to push changes to the remote repo
    * Rinse and repeat!

* [x] Download the starter project for this assessment and add the files to your repo
  * **See your instruction team for the correct files to use!**

* [x] Review the requirements (0.5 hours)

* [ ] Identify any research that I need to do (0.5 hours)

---

_Note: The following sequence of tasks is focused on keeping refactoring to a minimum. To that end, tasks related to user login and registration are to be completed before any tasks related to updating the Agents CRUD UI. This is necessary because the Secured Field Agent app requires a valid auth token for all Field Agent API HTTP requests._

### Part 1: Back-End Updates

_Use the SQL script included in the starter code to create your database and the initial data_

* [x] Create the AuthController with an endpoint for authenticating users (1.0 hours)

* [x] Update the security configuration ant matchers to allow anonymous requests to the AuthController endpoint for authenticating users (0.5 hours)
  - will give user the permission to only make get request whereas admin can make all 4. 

**Make sure that my GitHub repo is updated (i.e. push all commits to my local feature branch to the remote repo)!**

### Part 2: Client-Side Routes

* [x] Implement the required client-side routes (0.5 hours)
  * [x] Install `react-router-dom`
  * [x] Define the necessary client-side routes (see the list of routes below)
  * [x] Stub out any components that are needed to support the client-side routes
    * _Note: Stub out the individual Agents CRUD UI components but hold off on moving any code from last week's monolithic Agents CRUD UI component to the individual components_
  * [x] Display a "Not Found" message if a route doesn't match one of the defined routes

**Make sure that my GitHub repo is updated (i.e. push all commits to my local feature branch to the remote repo)!**

### Part 3: User Login

* [ x Create a "Login" component (1.0 hours)
  * [x] Add a form with "Username" and "Password" fields
  * [x] Use `fetch` to `POST` the user's information to the User API
  * [x] Use an existing user's information to test that the API returns an auth token

* [x] Add the necessary user state and helper functions to the App component (1.0 hours)
  * [x] Add a state variable to track the current user
  * [x] Add a function to login a user
  * [x] Add a function to logout a user
  * [x] Collect the user state variable along with the login/logout helper functions into a single `auth` object

* [x] Create a React context to track user auth (0.5 hours)
  * [x] Create the context in its own module
  * [x] Update the App component to provide the context to the entire app
  * [x] Set the context's `value` to the `auth` object

* [x] Update the "Login" component (1.0 hours)
  * [x] Consume the user auth context
  * [x] After a successful `POST` to the User API, call the auth context's login function and pass in the auth token and redirect the user to the "Home" route

* [x] Update the header/navbar component (1.0 hours)
  * [x] Consume the user auth context
  * [x] When there's a logged in user, display the user's username and "Logout" button
  * [x] When there's not a logged in user, display links for the "Login" and "Register" routes
  * [x] When the "Logout" button is clicked, call the auth context's logout function

* [x] Protect each of the agent related routes (0.5 hours)
  * [x] When there's a logged in user, display the route's associated component
  * [x] When there's not a logged in user, redirect the user to the login route

**Make sure that my GitHub repo is updated (i.e. push all commits to my local feature branch to the remote repo)!**

### Part 4: Agents CRUD UI Component Refactoring

* [x] Update the "Agents" component (0.5 hours)
  * [x] Update the "Add Agent" button to redirect the user to the "Add Agent" route (if not already completed)
  * [x] Update the individual agent "Edit" and "Delete" buttons to redirect the user to the appropriate routes (if not already implemented)

* [x] Update the "Add Agent" component (0.5 hours)
  * [x] Move code from the "Agents" component into the "Add Agent" component
  * [x] After a successful `POST` to the Field Agent API, redirect the user to the "Agents" route

* [x] Update the "Edit Agent" component (1.5 hours)
  * [x] Move code from the "Agents" component into the "Edit Agent" component
  * [x] Use the `useParams` hook to get the agent's ID from the route
  * [x] Use `fetch` to `GET` the agent from the Field Agent API when the component is first loaded
  * [x] After a successful `PUT` to the Field Agent API, redirect the user to the "Agents" route

* [x] Update the "Delete Agent" component (if needed) (1.0 hours)
  * [x] Move code from the "Agents" component into the "Delete Agent" component
  * [x] Use the `useParams` hook to get the agent's ID from the route
  * [x] Use `fetch` to `GET` the agent from the Field Agent API when the component is first loaded
  * [x] After a successful `DELETE` from the Field Agent API, redirect the user to the "Agents" route

**Make sure that my GitHub repo is updated (i.e. push all commits to my local feature branch to the remote repo)!**

### Part 5: Testing and Project Submission

* [x] Use the provided test plan to manually test the application (0.5 hours)

* [x] Create a pull request in GitHub to facilitate code review (0.05 hours)

---

## High-Level Requirements

Use the provided Secured Field Agent HTTP Service starter code as your back-end data service. Implement the new requirements by extending the React application from Module 8 with React Router, React Context, and additional components.

* Implement the AuthController with an endpoint for authenticating users.
* Update the security configuration ant matchers to allow anonymous requests to the AuthController endpoint for authenticating users.
* Implement the required client-side routes.
* Display a "Not Found" message if a route doesn't match one of the defined routes.
* Create new React components as needed to support the required client-side routes.
* Implement user login.
* Require a user to login to view the Agents CRUD UI.
* Display the logged in user's username in the header.
* Provide a way for the user to logout.

## Technical Requirements

* Use React Router to implement the client-side routes.
* Use React Context to share the current logged in user's information to any component that needs access to that information.
* Update the provided Backend to support adding user login and registration.
* Use React Router's `useHistory` hook to programmatically redirect users.
* Use React Router's `useParams` hook to access parameters, paths, and other data.

## Client-Side Routes

* "Home" `/` - Renders a component that displays a welcome message and a link to the "Agents" route
  * Links to other parts of the website could be added in the future
* "Agents" `/agents` - Renders a component that displays a list of agents
* "Add Agent" `/agents/add` - Renders a component that displays a form to add an agent
* "Edit Agent" `/agents/edit/:id` - Renders a component that displays a form to edit the agent specified by the `:id` route parameter
* "Delete Agent" `/agents/delete/:id` (optional) - Renders a component that displays a confirmation message to delete the agent specified by the `:id` route parameter
  * _Note: If this route isn't implemented, handle agent deletion within the "Agents" route._
* "Login" `/login` - Renders a component that displays a form to login a user
* "Not Found" - Renders a component that displays a friendly "not found" message if the requested route doesn't match one of the defined routes
