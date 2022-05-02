import AuthContext from "../AuthContext";
import Errors from "./Errors";

import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";

function UpdateFieldAgent() {

  const [firstName, setFirstName] = useState('');
  const [middleName, setMiddleName] = useState('');
  const [lastName, setLastName] = useState('');
  const [dob, setDob] = useState('');
  const [heightInInches, setHeightInInches] = useState(0);
  const [errors, setErrors] = useState([]);
  const [init, setInit] = useState(false);

  const history = useHistory();
  const authManager = useContext(AuthContext);
  const { id } = useParams();

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

  useEffect(() => {
    fetch(`http://localhost:8080/api/agent/${id}`, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt_token')}`}})
      .then(response => {
        if (response.status === 200) {
          return response.json();
        }

        if (response.status === 404) {
          history.update('/not-found');
          return null;
        }

        return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        if (body) {
          setInit(true);
          setFirstName(body.firstName);
          setMiddleName(body.middleName);
          setLastName(body.lastName);
          setDob(body.dob);
          setHeightInInches(body.heightInInches);
        }
      })
      .catch(err => console.error(err));
  }, [history, id]);

  const handleSubmit = (event) => {
    event.preventDefault();

    const updateFieldAgent = {
      agentId: id,
      firstName,
      middleName,
      lastName,
      dob,
      heightInInches
    };

    const init = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      },
      body: JSON.stringify(updateFieldAgent)
    };

    fetch(`http://localhost:8080/api/agent/${id}`, init)
      .then(response => {
        switch (response.status) {
          case 204:
            return null;
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
      .then(body => {
        if (!body) {
          history.push('/agents');
          return;
        }

        setErrors(body);
      })
      .catch(err => console.error(err));
  }


  return (
    <>
      {init ? (<>
        <h2 className="mt-5">Update FieldAgent</h2>
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
            <button className="btn btn-primary" type="submit" onClick={handleSubmit}>Update FieldAgent</button>
            &nbsp;
            <button className="btn btn-secondary" type="button" onClick={() => history.push('/agents')}>Cancel</button>
          </div>
        </form>
        <Errors errors={errors} />
      </>) : null}
    </>
  )

}

export default UpdateFieldAgent;