import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter,Routes,Route } from "react-router-dom";
import Banner from './components/Banner';
import Footer from './components/Footer';

import Login from "./components/Login";
 
import Home from "./components/Home";
function App() {
  return (
    /*
    <div className="App">
      <Banner/>
      <Login/>
      <Footer/>
    </div>*/
    <div>
      <BrowserRouter>
            <Routes>
              <Route path="/home" element= { <Home/>} />
              <Route path="/" element= { <Login/>} />
            </Routes>
        </BrowserRouter>
      
    </div>
  );
}

export default App;
