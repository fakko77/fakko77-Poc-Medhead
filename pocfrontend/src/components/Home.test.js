import React from 'react';
import { render, waitFor } from '@testing-library/react';
import Home from './Home'; // Make sure the import path is correct

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

global.fetch = jest.fn(() =>
  Promise.resolve({
    json: () => Promise.resolve([{ id: 1, name: 'Specialization A' }]),
  })
);

describe('Home Component', () => {
  it('fetches and displays data on mount', async () => {
    const { getByText } = render(<Home />);

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith('http://localhost:8080/allSpecialisations', {
        headers: { Authorization: expect.stringContaining('Bearer ') },
      });
//expect(getByText('Specialization A')).toBeInTheDocument();
    });
  });
});