import AuthContext from "../AuthContext";
import Errors from "./Errors";

import { useState, useContext } from "react";
import { useHistory } from "react-router-dom";

function AddFieldAgent() {
  const [firstName, setFirstName] = useState('');
  const [middleName, setMiddleName] = useState('');
  const [lastName, setLastName] = useState('');
  const [dob, setDob] = useState(new Date());
  const [heightInInches, setHeightInInches] = useState(0);
  const [errors, setErrors] = useState([]);

  const history = useHistory();
  const authManager = useContext(AuthContext);

  const handleFirstNameChange = (event) => {
    setFirstName(event.target.value);
  }

  const handleMiddleNameChange = (event) => {
    setMiddleName(event.target.value);
  }

  const handleLastNameChange = (event) => {
    setLastName(event.target.value);
  }

  const handleDoBChange = (event) => {
    setDob(event.target.value);
  }

  const handleHeightInInchesChange = (event) => {
    setHeightInInches(event.target.value);
  }


  const handleSubmit = (event) => {
    event.preventDefault();

    const newFieldAgent = {
      firstName,
      middleName,
      lastName,
      dob,
      heightInInches
    };

    const init = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      },
      body: JSON.stringify(newFieldAgent)
    };

    fetch('http://localhost:8080/api/agent', init)
      .then(response => {
        switch (response.status) {
          case 201:
          case 400:
            return response.json();
          case 403:
            authManager.logout();
            history.push('/login');
            break;
          default:
            return Promise.reject('Something went wrong on the server :)');
        }
      })
      .then(json => {
        if (json.agentId) {
          history.push('/agents');
        } else {
          setErrors(json);
        }
      })
      .catch(err => console.error(err));
  }

  return (
    <>
      <h2 className="mt-5">Add FieldAgent</h2>
      <form>
        <div className="form-group">
          <label htmlFor="firstName">First Name:</label>
          <input className="form-control" type="text" id="firstName" name="firstName" value={firstName} onChange={handleFirstNameChange} ></input>
          <label htmlFor="middleName">Middle Name:</label>
          <input className="form-control" type="text" id="middleName" name="middleName" value={middleName} onChange={handleMiddleNameChange} ></input>
          <label htmlFor="lastName">Last Name:</label>
          <input className="form-control" type="text" id="lastName" name="lastName" value={lastName} onChange={handleLastNameChange} ></input>
          <label htmlFor="dob">Date of Birth:</label>
          <input className="form-control" type="date" id="dob" name="dob" value={dob} onChange={handleDoBChange} ></input>
          <label htmlFor="heightInInches">Height in Inches:</label>
          <input className="form-control" type="number" id="heightInInches" name="heightInInches" value={heightInInches} onChange={handleHeightInInchesChange} ></input>
        </div>
        <div className="form-group">
          <button className="btn btn-primary" type="submit" onClick={handleSubmit}>Add FieldAgent</button>
          &nbsp;
          <button className="btn btn-secondary" type="button" onClick={() => history.push('/agents')}>Cancel</button>
        </div>   
      </form>
      <Errors errors={errors}/>
    </>
  )

}

export default AddFieldAgent;