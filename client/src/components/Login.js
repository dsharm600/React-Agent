import { useContext, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";
import Errors from "./Errors";

const DEFAULT_LOGIN = {
  username: '',
  password: ''
}

function Login() {

  const [credentials, setCredentials] = useState(DEFAULT_LOGIN);
  const [errors, setErrors] = useState([]);

  const authManager = useContext(AuthContext);
  const history = useHistory();

  const handleChange = (event) => {
    // create a replacement
    const replacmentCredentials = { ...credentials };

    replacmentCredentials[event.target.name] = event.target.value;

    setCredentials(replacmentCredentials);
  }

  const handleSubmit = async (event) => {
    event.preventDefault();

    debugger;

    const init = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(credentials)
    }

    fetch('http://localhost:8080/api/authenticate', init)
      .then(resp => {
        console.log(resp);
        if (resp.status === 200) {
          return resp.json();
        }

        if (resp.status === 403) {
          return null;
        }

        return Promise.reject('Something went wrong on the server :)');
      })
      .then(json => {
        if (json) {
          authManager.login(json.jwt_token);
          history.push("/");
        } else {
          setErrors(['The supplied login information does not appear to be in our system.'])
        }

        
      })
      .catch(err => console.error(err));

  }

  return (
    <>
      <h2>Login</h2>
      <form>
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input className="form-control" type="text" id="username" name="username" value={credentials.username} onChange={handleChange} ></input>
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input className="form-control" type="password" id="password" name="password" value={credentials.password} onChange={handleChange} ></input>
        </div>
        <div className="form-group">
          <button className="btn btn-primary" type="submit" onClick={handleSubmit}>Login</button>
        </div>
      </form>
      <Errors errors={errors}/>
    </>
  )

}

export default Login;