import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../AuthContext";

function NavBar() {

  const authManager = useContext(AuthContext);

  const handleLogout = () => {
    authManager.logout();
  }

  return (
    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
      <span className="navbar-brand" >Field Agent App</span>
      <div className="navbar-collapse">
        <ul className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link className="nav-link" to="/">Home</Link>
          </li>
          <li className="nav-item" >
            <Link className="nav-link" to="/about">About</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/agents">Field Agents</Link>
          </li>
          {authManager.user ? (
            <li className="nav-item">
              <button className="btn btn-secondary" type="button" onClick={handleLogout}>Logout</button>
            </li>
          )

            : (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">Login</Link>
                </li>
              </>
            )}


        </ul>
      </div>
      {authManager.user ? <span className="text-light navbar-text">Hello, {authManager.user}</span> : null}
    </nav>
  );
}

export default NavBar;