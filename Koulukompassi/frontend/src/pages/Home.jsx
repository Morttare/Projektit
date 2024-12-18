import React from 'react';
import MapComponent from '../components/Map/MapComponent';
import { useLocation } from 'react-router-dom';
import './Home.css';

const Home = (route, navigation) => {
    const location = useLocation();
    const {type} = location.state || {};

    return (
        <div className={"home"}>
            <h1 className={"title"}>Koulu-Kompassi</h1>
            <MapComponent type={type}/>
        </div>
    );
};

export default Home;