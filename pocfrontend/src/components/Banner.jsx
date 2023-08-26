import { Alert } from 'bootstrap';
import logo from '../assets/logo.png';

export default function Banner() {
  const rgpd = () => {
    alert("En vertu du RGPD, notre organisation est tenue de stocker les données personnelles pendant une durée limitée et justifiée. Pour toute demande de suppression, veuillez contacter notre administrateur admin@medhead.com");
  };

  return (
    <div>
      <nav className="navbar bg-custom">
        <div className="container-fluid">
          <a className="navbar-brand" href="#">
            <img src={logo} alt="Logo of banner" width="30" height="24" className="d-inline-block align-text-top"/>
            <h3>MedHead</h3>
          </a>
          <h2 onClick={rgpd}>
          ℹ️
          </h2>
        </div>
      </nav>
    </div>
  );
}