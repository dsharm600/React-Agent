import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";


function FieldAgents() {

  // create state for the agents
  const [fieldAgents, setFieldAgents] = useState([]);

  const history = useHistory();

  const authManager = useContext(AuthContext);

  let getFieldAgent = () => {
    return fetch('http://localhost:8080/api/agent')
      .then(response => {
        if (response.status === 200) {
          return response.json()
        }

        return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        setFieldAgents(body);
      })
      .catch(err => console.error(err));
  }

  // use effect will fire as soon as the page is done loading
  useEffect(() => {
    getFieldAgent();
  }, []);

  const handleAddSelect = () => {
    history.push('/agents/add');
  }

  const handleEditSelect = (fieldAgent) => {
    history.push(`/agents/edit/${fieldAgent.agentId}`);
  }

  const handleDeleteSelect = (fieldAgent) => {
    history.push(`/agents/delete/${fieldAgent.agentId}`);
  }

    return (
        <>
                <h2 className="mt-5">FieldAgents List</h2>
                {authManager.user ? <button className="btn btn-primary mb-3 mt-4" type="button" onClick={handleAddSelect}>Add FieldAgent</button> : null}
                <table className="table table-sm">
                    <thead>
                        <tr>
                            <th scope="col"></th>
                            <th scope="col">#</th>
                            <th scope="col">Full Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {fieldAgents.map((fa, i) => (
                            <tr key={fa.agentId}>
                                <td>
                                {authManager.user ? (<>
                                    <button className="btn btn-warning" type="button" onClick={() => handleEditSelect(fa)}>Edit</button>
                                    &nbsp;
                                    {authManager.hasRole('admin') ? <button className="btn btn-danger" type="button" onClick={() => handleDeleteSelect(fa)}>Delete</button>  : null }
                                    </>) : null}
                                </td>
                                <td>
                                    &nbsp;
                                    {i + 1}
                                </td>
                                <td>
                                    {fa.firstName + " " + fa.middleName + " " + fa.lastName}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </>
    )

}

export default FieldAgents;