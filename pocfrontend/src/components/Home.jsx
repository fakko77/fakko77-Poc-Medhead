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


  const [address, setAddress] = useState('');

  const handleInputChange = (e) => {
    setAddress(e.target.value);
  };


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

  const sortJSONArray = (jsonArray, columnName) => {
    jsonArray.sort((a, b) => {
        const value1 = a[columnName];
        const value2 = b[columnName];
        
        // Assurez-vous que les valeurs sont comparables (par exemple, numériques)
        if (value1 < value2) {
            return -1;
        }
        if (value1 > value2) {
            return 1;
        }
        return 0;
    });
};

  const getInfo = async (name) => {
    const loc = localStorage.getItem('loc');
      try {
        const rawResponse = await fetch('http://localhost:8080/allInfo', {
          method: 'POST',
          headers: {
            Authorization: 'Bearer ' + jwt,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ coordinates: loc, specialisationName: name})
        });
  
      const jsonData = await rawResponse.json();

      sortJSONArray(jsonData, "distance");
      setHospitalInfo(jsonData); // Assurez-vous d'avoir défini setHospitalInfo correctement.
      console.log(jsonData);

    } catch (error) {
      console.error(error);
    }
  };
  


  const handleSubmit = async (e) => { // Ajoutez "async" ici pour pouvoir utiliser "await"
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/getlocation/' + address, {
        headers: { Authorization: 'Bearer ' + jwt }
      });
      const jsonData = await response.json();

      if (jsonData.error == 'Erreur lors de la récupération des coordonnées GPS.'){
        alert("Erreur lors de la récupération des coordonnées GPS.");
      }else{
        console.log(jsonData);
        var loc = jsonData["longitude"]+","+jsonData["latitude"];
        console.log(loc);
        localStorage.setItem("loc",loc);
        $(".adresseShow").hide();
        $(".blocApp").show();
      }
      

    } catch (err) {
      alert(err);
    }
    // Vous pouvez traiter l'adresse ici, par exemple, l'envoyer à un backend ou l'enregistrer localement.
    
    console.log(address);
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
      <div className='adresseShow'>
      <form onSubmit={handleSubmit} className='adresse'>
      <label>
        Adresse:
        <input
          type="text"
          value={address}
          onChange={handleInputChange}
          placeholder="Entrez votre adresse"
        />
      </label>
      <button type="submit">Soumettre</button>
    </form>
      </div>
      <div className="blocApp" style={{display:'none'}}>
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
                <th>Distance</th>
              </tr>
            </thead>
            <tbody>
              {hospitalInfo.map((item, index) => (
                <tr key={item.id}>
                  <td>{item.Hospital}</td>
                  <td>{item.available}</td>
                  <td>{item.distance}</td>
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