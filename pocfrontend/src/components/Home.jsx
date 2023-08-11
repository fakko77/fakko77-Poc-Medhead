
import Button from 'react-bootstrap/Button';
import $ from "jquery";
import React, { useState, useEffect } from 'react';
import Banner from './Banner';
import Footer from './Footer';
import Table from 'react-bootstrap/Table';

export default function Home(){
    var jwt = localStorage.getItem("jwt");
    const [data, setData] = useState([]);
    const [hospitalInfo, sethospitalInfo] = useState([]);
    useEffect(() => {
        
        const fetchData = async () => {
            try {
              const response = await fetch('http://localhost:8080/allSpecialisations', {
                headers: {Authorization: 'Bearer ' +jwt}
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


        async function getInfo( name) {
         
            try {
                const response = await fetch('http://localhost:8080/allInfo/'+name, {
                    headers: {Authorization: 'Bearer ' +jwt}
                  });
                  console.log("iciii", jwt);
                  const jsonData = await response.json();
                  sethospitalInfo(jsonData);
                  console.log(jsonData);
            }
        
             catch (err) {
              alert(err);
            }
          
          }


          async function add( index, id) {

            try {
              const response = await fetch('http://localhost:8080/book/'+id, {
                  headers: {Authorization: 'Bearer ' +jwt}
                });
 
                const jsonData = await response.json();
                console.log(jsonData);

                if(jsonData[0].message == "Good booK"){
                  const nouvelleValeur = hospitalInfo[index].available ;   
                  const nouveauxItems = [...hospitalInfo];
                  nouveauxItems[index].available = nouvelleValeur - 1;
                  sethospitalInfo(nouveauxItems);
                  var reservation = "reservation"+id;
                  var annulation = "annulation"+id;
                  $("#"+reservation).hide();
                  $("#"+annulation).show();
                  alert("Réservation  prise en compte");
                }else {

                  alert("imposible pas de place dispo ");
                }
          }
      
           catch (err) {
            alert(err);
          }


          }

          async function addCancel( index, id) {

            try {
              const response = await fetch('http://localhost:8080/CancelBook/'+id, {
                  headers: {Authorization: 'Bearer ' +jwt}
                });
 
                const jsonData = await response.json();
                console.log(jsonData);

                if(jsonData[0].message == "Cancel book good"){
                  const nouvelleValeur = hospitalInfo[index].available ;   
                  const nouveauxItems = [...hospitalInfo];
                  nouveauxItems[index].available = nouvelleValeur + 1;
                  sethospitalInfo(nouveauxItems);
                  var reservation = "reservation"+id;
                  var annulation = "annulation"+id;
                  $("#"+reservation).show();
                  $("#"+annulation).hide();
                  alert("Annulation prise en compte  prise en compte");
                }else {

                  alert("imposible pas de place dispo ");
                }
          }
      
           catch (err) {
            alert(err);
          }

          }

    return (
    <div>
             <Banner/>
            
     
        <div className='blocApp'>
        <Table striped bordered hover style={{width: '20%',}}>
        <thead>
          <tr>
            <th>Spécialisation</th>
          </tr>
        </thead>
        <tbody>
          {data.map(item => (
            <tr key={item.id}>
              <td>{item.name}</td>
              <td> <Button variant="info" size="sm"  type="submit" onClick={() => getInfo( item.name)}>Afficher les hopitaux </Button></td>     
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
            <tr key={item.id}  >
              <td>{item.Hospital}</td>
              <td>{item.available}</td>
              <td id= {"reservation" + item.id} ><Button  variant="success" size="sm"  type="submit" onClick={() => add(index, item.id)}> Réserver </Button></td>
              <td id= {"annulation" + item.id} style={{display: 'none' }} > <Button variant="danger" size="sm"  type="submit" onClick={() => addCancel(index, item.id)}> Annulation </Button></td>
               </tr>
          ))}
        </tbody>
        </Table></div>

        </div>
             <Footer/>
     
    </div>);
}