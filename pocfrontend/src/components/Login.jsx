import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import { act } from 'react-dom/test-utils';
import Banner from './Banner';
import Footer from './Footer';

import logo from '../assets/img-login.png';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const rawResponse = await fetch('http://localhost:8081/app/login', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
      });

      const content = await rawResponse.json();
      if (content.tokenType === 'Bearer') {
        navigate('/home');
      } else {
        alert('Login incorrect');
      }
      console.log(content);
    } catch (err) {
      alert(err);
    }
  };

  return (
    <>
      <Banner />
      <div className="App">
        <img src={logo} className="logo-img" alt="img médical" />
        <Container className="login-container">
          <Form className="justify-content-md-center" onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>
                <h3>Username</h3>
              </Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={(event) => act(() => {setUsername(event.target.value)})}
                data-testid="username-input"
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
              <Form.Label>
                <h3>Password</h3>
              </Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                value={password}
                onChange={(event) =>   act(() => {setPassword(event.target.value)})}
                data-testid="password-input"
              />
            </Form.Group>
            <Button variant="primary" type="submit">
              Connexion
            </Button>
          </Form>
        </Container>
      </div>
      <Footer />
    </>
  );
};

export default Login;
