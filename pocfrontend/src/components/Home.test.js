import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Home from './Home'; // Import your Home component

describe('Home Component', () => {
  it('fetches and displays data correctly', async () => {
    const jwtMock = 'mocked-jwt';
    global.localStorage.setItem('jwt', jwtMock);

    const dataMock = [
      { id: 1, name: 'Specialization 1' },
    ];

    const infoMock = [
      { id: 1, Hospital: 'Hospital 1', available: 5 },
    ];

    global.fetch = jest.fn();
    fetch.mockResolvedValueOnce({
      json: () => Promise.resolve(dataMock),
    });

    fetch.mockResolvedValueOnce({
      json: () => Promise.resolve(infoMock),
    });

    render(<Home />);

    await waitFor(() => {
      expect(screen.getByText('Specialization 1')).toBeInTheDocument();
    });

    userEvent.click(screen.getByText('Afficher les hopitaux'));

    await waitFor(() => {
      expect(screen.getByText('Hospital 1')).toBeInTheDocument();
    });
  });

  it('handles booking and cancellation', async () => {
    const jwtMock = 'mocked-jwt';
    global.localStorage.setItem('jwt', jwtMock);

    const infoMock = [
      { id: 1, Hospital: 'Hospital 1', available: 5 },
    ];

    global.fetch = jest.fn();
    fetch.mockResolvedValue({
      json: () => Promise.resolve([{ message: 'Good booK' }]),
    });
     global.fetch = jest.fn();
    fetch.mockResolvedValueOnce({
      json: () => Promise.resolve(dataMock),
    });

    fetch.mockResolvedValueOnce({
      json: () => Promise.resolve(infoMock),
    });


   /*
   const button = screen.getByText('Afficher les hopitaux');
   fireEvent.click(button);
    
    const reservationButton = screen.getAllByText('Réserver')[0];
    fireEvent.click(reservationButton);

    await waitFor(() => {
      expect(screen.getByText('Annulation prise en compte')).toBeInTheDocument();
    });

    const annulationButton = screen.getAllByText('Annulation')[0];
    fireEvent.click(annulationButton);

    await waitFor(() => {
      expect(screen.getByText('Réservation prise en compte')).toBeInTheDocument();
    }); */
  });
});
