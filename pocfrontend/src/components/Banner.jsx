import logo from '../assets/logo.png';


export default function Banner(){

    return (
    <div>
    <nav className="navbar bg-custom">
    <div className="container-fluid">
      <a className="navbar-brand" href="#">
        <img src={logo} alt="Logo of banner" width="30" height="24" className="d-inline-block align-text-top"/>
        <h3>MedHead</h3>
      </a>
    </div>
  </nav></div>);
}