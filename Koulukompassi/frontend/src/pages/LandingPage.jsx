import React from 'react';
import {Button, Row, Col } from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import '../LandingPage.css';
import compassImage from "../assets/compassIcon.png";


const LandingPage = () => {
    const navigate = useNavigate();
    return (
        <div className="landing-container" style={{padding: "25px"}}>
            <img
                src={compassImage}
                alt="compass icon"
                style={{width: '400px', height: '400px', marginBottom: '8px'}}
            />
            <header className="text-center landing-title">
                <h1 className="landing-title"> Koulu-Kompassi</h1>
                <h1 className="landing-title"> High School & College/University Info App</h1>
            </header>
            <p>Select an option to view information from schools on the map:</p>
            <div className="d-flex justify-content-center align-items-center landing-buttons">
                <Row className="text-center">
                    <Col>
                        <Button variant="primary" size="lg" className="buttons"
                                onClick={() => navigate('/home', {state: {type: "high_school"}})}>
                            High Schools
                        </Button>
                        <Button variant="primary" size="lg" className="buttons"
                                onClick={() => navigate('/home', {state: {type: "college"}})}>
                            Colleges / Universities
                        </Button>
                    </Col>
                </Row>
            </div>
        </div>
    );
}
export default LandingPage;