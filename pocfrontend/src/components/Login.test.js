import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useNavigate } from 'react-router-dom';
import Login from './Login';
import { act } from 'react-dom/test-utils';

window.alert = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

global.fetch = jest.fn(() =>
  Promise.resolve({
    json: () => Promise.resolve({ tokenType: 'Bearer' }),
  })
);

describe('Login Component', () => {
  it('handles successful login', async () => {
    const navigateMock = jest.fn();
    useNavigate.mockReturnValue(navigateMock);

    const { getByTestId, getByText } = render(<Login />);

    userEvent.type(getByTestId('username-input'), 'testuser');
    userEvent.type(getByTestId('password-input'), 'testpassword');
    fireEvent.click(getByText('Connexion'));

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledWith('http://localhost:8081/app/login', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ username: 'testuser', password: 'testpassword' }),
        });
      
        expect(navigateMock).not.toBeNull();

      });
  });


  
  it('handles incorrect login', async () => {
    const navigateMock = jest.fn();
    useNavigate.mockReturnValue(navigateMock);

    const { getByTestId, getByText } = render(<Login />);

    userEvent.type(getByTestId('username-input'), 'testuser');
    userEvent.type(getByTestId('password-input'), 'testpassword');

    global.fetch.mockImplementationOnce(() =>
      Promise.resolve({
        json: () => Promise.resolve({ tokenType: 'Invalid' }),
      })
    );

    global.alert = jest.fn();

    fireEvent.click(getByText('Connexion'));

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith('http://localhost:8081/app/login', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: 'testuser', password: 'testpassword' }),
      });

      expect(global.alert).toHaveBeenCalledWith('Login incorrect');
    });
  });



  it('handles login error', async () => {
    const navigateMock = jest.fn();
    useNavigate.mockReturnValue(navigateMock);

    const { getByTestId, getByText } = render(<Login />);

    userEvent.type(getByTestId('username-input'), 'testuser');
    userEvent.type(getByTestId('password-input'), 'testpassword');

    global.fetch.mockImplementationOnce(() => Promise.reject(new Error('Network error')));

    global.alert = jest.fn();

    fireEvent.click(getByText('Connexion'));

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith('http://localhost:8081/app/login', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: 'testuser', password: 'testpassword' }),
      });
      
      expect(global.alert).toHaveBeenCalledWith(new Error('Network error'));
    });
  });
});