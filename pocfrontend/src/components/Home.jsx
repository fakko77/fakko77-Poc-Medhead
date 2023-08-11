import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Table from 'react-bootstrap/Table';
import $ from 'jquery'; 

import Banner from './Banner';
import Footer from './Footer';

export default function Home() {
  const jwt = localStorage.getItem('jwt');
  const [data, setData] = useState([]);
  const [hospitalInfo, setHospitalInfo] = useState([]);


  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/allSpecialisations', {
          headers: { Authorization: 'Bearer ' + jwt }
        });
        const jsonData = await response.json();
        setData(jsonData);
        console.log(jsonData);
      } catch (error) {
        console.error('Erreur lors de la récupération des données :', error);
      }
    };

    fetchData();
  }, []);

  const getInfo = async (name) => {
    try {
      const response = await fetch('http://localhost:8080/allInfo/' + name, {
        headers: { Authorization: 'Bearer ' + jwt }
      });
      const jsonData = await response.json();
      setHospitalInfo(jsonData);
      console.log(jsonData);
    } catch (err) {
      alert(err);
    }
  };

  const handleBooking = async (index, id, action) => {
    try {
      const response = await fetch('http://localhost:8080/' + action + '/' + id, {
        headers: { Authorization: 'Bearer ' + jwt }
      });

      const jsonData = await response.json();
      console.log(jsonData);

      if (jsonData[0].message === 'Good booK' && action === 'book') {
        const newAvailableValue = hospitalInfo[index].available - 1;
        const newItems = [...hospitalInfo];
        newItems[index].available = newAvailableValue;
        setHospitalInfo(newItems);
        const reservationId = 'reservation' + id;
        const annulationId = 'annulation' + id;
        $("#" + reservationId).hide();
        $("#" + annulationId).show();
        alert('Réservation prise en compte');
      } else if (jsonData[0].message === 'Cancel book good' && action === 'CancelBook') {
        const newAvailableValue = hospitalInfo[index].available + 1;
        const newItems = [...hospitalInfo];
        newItems[index].available = newAvailableValue;
        setHospitalInfo(newItems);
        const reservationId = 'reservation' + id;
        const annulationId = 'annulation' + id;
        $("#" + reservationId).show();
        $("#" + annulationId).hide();
        alert('Annulation prise en compte');
      } else {
        alert('Impossible, pas de place disponible');
      }
    } catch (err) {
      alert(err);
    }
  };

  return (
    <div>
      <Banner />
      <div className="blocApp">
        <Table striped bordered hover style={{ width: '20%' }}>
          <thead>
            <tr>
              <th>Spécialisation</th>
            </tr>
          </thead>
          <tbody>
            {data.map(item => (
              <tr key={item.id}>
                <td>{item.name}</td>
                <td>
                  <Button variant="info" size="sm" type="button" onClick={() => getInfo(item.name)}>
                    Afficher les hopitaux
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
        <div>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Nom</th>
                <th>Nb lits</th>
              </tr>
            </thead>
            <tbody>
              {hospitalInfo.map((item, index) => (
                <tr key={item.id}>
                  <td>{item.Hospital}</td>
                  <td>{item.available}</td>
                  <td id={'reservation' + item.id}>
                    <Button
                      variant="success"
                      size="sm"
                      type="button"
                      onClick={() => handleBooking(index, item.id, 'book')}
                    >
                      Réserver
                    </Button>
                  </td>
                  <td id={'annulation' + item.id} style={{ display: 'none' }}>
                    <Button
                      variant="danger"
                      size="sm"
                      type="button"
                      onClick={() => handleBooking(index, item.id, 'CancelBook')}
                    >
                      Annulation
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
      <Footer />
    </div>
  );
}