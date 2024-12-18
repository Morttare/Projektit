import React from "react";
import {Marker, Popup} from "react-leaflet";
import starImage from "../../assets/star.png";
import emptyStarImage from "../../assets/emptyStar.png";
import {Button} from "react-bootstrap";

const MapMarker = ({ item, togglePanel, toggleFavourite, favourites }) => {
    const [isStarColored, setIsStarColored] = React.useState(false);

    // Is star empty or filled
    React.useEffect(() => {
        const shouldBeColored = favourites.includes(item.schoolName);
        if(isStarColored !== shouldBeColored) {
            setIsStarColored(shouldBeColored);
        }
    }, [favourites, item.schoolName, isStarColored]);


    const handleStarClick = () => {
        toggleFavourite(item.schoolName);
    };


    const { lat, lon } = item.coord;

    return (
        <Marker position={[lat, lon]}>
            <Popup>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <img
                        src={isStarColored ? starImage : emptyStarImage}
                        alt="star icon"
                        onClick={handleStarClick}
                        style={{ width: '24px', height: '24px', marginRight: '8px', cursor: 'pointer' }}
                    />
                    <Button onClick={() => togglePanel(item.schoolName, false)}>
                        <strong>{item.campusName}</strong>
                    </Button>
                </div>
            </Popup>
        </Marker>
    );
};

export default MapMarker;