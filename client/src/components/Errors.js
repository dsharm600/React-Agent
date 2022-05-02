function Errors({ errors }) {

    return (
      <>
        {errors.length ? (
          <div className="alert alert-danger">
            <span>The following Errors occurred:</span>
            <ul>
              {errors.map((err, i) => <li key={i}>{err}</li>)}
            </ul>
          </div>) : null
        }
      </>
    )
  }
  
  export default Errors;