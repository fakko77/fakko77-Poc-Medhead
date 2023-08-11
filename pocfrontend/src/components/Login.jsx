import logo from '../assets/img-login.png';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import {  useState } from "react";
import { useNavigate } from "react-router-dom";

import axios from "axios";
import Banner from './Banner';
import Footer from './Footer';


export default function Login(){
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");



  async function login(event) {
    event.preventDefault();
    try {
      const rawResponse = await fetch('http://localhost:8081/app/login', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({username: username, password: password})
      });

      const content = await rawResponse.json();
      if(content.tokenType == "Bearer"){
        navigate("/home");
      }else{
        alert('Login incorrect')
      }
      console.log(content);
    }

     catch (err) {
      alert(err);
    }
  
  }

  /*      await axios.post("http://localhost:8081/app/login", {
        username: username,
        password: password,
        }).then((res) => 
        {
         console.log(res.data);
         
         if(res.data.tokenType == "Bearer")
         { 
            
          console.log("oook");
          localStorage.setItem("jwt", res.data.accessToken);
          alert("Login is good");
          navigate("/home");
         } 
          else 
         { 
            alert("Incorrect Email and Password not match");
         }
      }, fail => {
       console.error(fail); 
       alert("Incorrect Username and Password not match");// Error!
});*/

    return (
      
    <div>
       <Banner/>
     <div className="App"  style={{
      fontfamily: "Montserrat",
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      flexDirection: 'column',
      height: '80vh',
    }}>
      <img src={logo} style={{ width: '35%', boxShadow: '0px 0px 5px',}} alt='img médical'></img>
    
      <Container style={{ width: '25%', fontWeight: 'bold', fontSize:'1.1rem', paddingTop:'2%' }}>
      <Form className="justify-content-md-center" >
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label > <h3>Username</h3> </Form.Label>
          <Form.Control type="text" placeholder="Enter username"  value={username}
          onChange={(event) => {
            setUsername(event.target.value);
          }} />
          <Form.Text className="text-muted">
            
          </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label><h3>Password</h3></Form.Label>
          <Form.Control type="password" placeholder="Password"   value={password}
            onChange={(event) => {
              setPassword(event.target.value);
            }}
            />
        </Form.Group>
        <Button variant="primary" type="submit" onClick={login} >
          Connexion
        </Button>
      </Form>
      </Container>

    </div>
    <Footer/>
    </div>);
}