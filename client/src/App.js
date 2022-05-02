import NavBar from "./components/NavBar";
import FieldAgents from "./components/FieldAgents";
import Add from "./components/AddFieldAgent";
import Update from "./components/UpdateFieldAgent";
import Delete from "./components/DeleteFieldAgent";
import NotFound from "./components/NotFound";
import Login from "./components/Login";
import AuthContext from "./AuthContext";

import { Route, Switch } from "react-router-dom";
import { useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import jwtDecode from "jwt-decode";

const TOKEN = 'jwt_token';

function App() {
  const [init, setInit] = useState(false);
  const [authManager, setAuthManager] = useState({
    user: null,
    roles: null,
    login(token) {
      if (!this.user) {
        const userData = jwtDecode(token);
        localStorage.setItem(TOKEN, token);
        setAuthManager((prevState) => ({...prevState, user: userData.sub, roles: userData.authorities}));
      }
    },
    logout() {
      if (this.user) {
        localStorage.removeItem(TOKEN);
        setAuthManager((prevState) => ({...prevState, user: null, roles: null}));
      }
    },
    hasRole(role) {
      return this.roles && this.roles.split(',').includes(`ROLE_${role.toUpperCase()}`);
    }
  });

  useEffect(() => {
    const token = localStorage.getItem(TOKEN);
    if (token) {
      authManager.login(token);
    }
    setInit(true);
  }, [authManager]);

  return (
    <div className="App">
      {init ? 
      (<AuthContext.Provider value={authManager} >
        <NavBar />
        <div className="container">
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route  path="/about">
              <About />
            </Route>

            <Route exact path="/agents" >
            {authManager.user ? <FieldAgents /> : <Redirect to="/login" /> }
            </Route>
            <Route  path="/agents/add" >
              {authManager.user ? <Add /> : <Redirect to="/login" /> }
            </Route>
            <Route  path="/agents/edit/:id" >
              {authManager.user ? <Update />  : <Redirect to="/login" /> }
            </Route>
            <Route  path="/agents/delete/:id" >
              {authManager.hasRole('admin')  ? <Delete />  : <Redirect to="/login" /> }
            </Route>
            <Route path="/login" >
              {authManager.user ? <Redirect to="/" /> : <Login />}
            </Route>
            <Route>
              <NotFound />
            </Route>
          </Switch>   
        </div>
      </AuthContext.Provider>) : null}
    </div>
  );
}

function Home() {
  return (
    <>
      <h2>Home</h2>
      <div>
        <p>Welcome to the Field Agents app, agent.</p>
      </div>
    </>
  )
}

function About() {
  return (
    <>
      <h2>About</h2>
      <div>
        <p>About the Creator</p>
        <ul>
        <li>I like long walks on the beach</li>
        </ul>
      </div>
    </>
  )
}


export default App;