import { useHistory, useParams } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import AuthContext from '../AuthContext';

function DeleteFieldAgent() {

  const [firstName, setFirstName] = useState('');
  const [middleName, setMiddleName] = useState('');
  const [lastName, setLastName] = useState('');
  const [dob, setDob] = useState(null);
  const [heightInInches, setHeightInInches] = useState(0);
  const [init, setInit] = useState(false);

  const history = useHistory();
  const { id } = useParams();
  const authManager = useContext(AuthContext);

  useEffect(() => {
    fetch(`http://localhost:8080/api/agent/${id}`)
      .then(response => {
        if (response.status === 200) {
          return response.json();
        }

        if (response.status === 404) {
          history.replace('/not-found');
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
  }, []);

  const handleSubmit = () => {

    const init = {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      }
    }
    
    fetch(`http://localhost:8080/api/agent/${id}`, init)
      .then(response => {
        if (response.status === 204) {
          history.push('/agents');
          return;
        } else if (response.status === 403) {
          authManager.logout();
          history.push('/login');
        }

        return Promise.reject('Something went wrong :)');
      })
      .catch(err => console.error(err));
  }

  return (
    <>
      {init ? (<>
        <h2>FieldAgent Delete</h2>
        <div className="alert alert-warning">
          <div>Are you sure you want to delete this agent?</div>
          <div>FieldAgent: {firstName + " " + middleName + " " + lastName}</div>
          <div>Date of Birth: {dob}</div>
          <div>Height in Inches: {heightInInches}</div>
        </div>
        <button className="btn btn-primary" type="button" onClick={handleSubmit}>Delete</button>
        &nbsp;
        <button className="btn btn-secondary" type="button" onClick={() => history.push('/agents')}>Cancel</button>
      </>) : null}
    </>
  );


}

export default DeleteFieldAgent;