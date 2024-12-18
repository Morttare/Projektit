import React from 'react';

const TypeFilter = ({ dataType, types, selectedTypes, setSelectedTypes }) => {
    if (dataType !== "college") {return}

    const handleCheckboxChange = (type) => {
        if (selectedTypes.includes(type)) {
            setSelectedTypes(selectedTypes.filter((f) => f !== type));
        } else {
            setSelectedTypes([...selectedTypes, type]);
        }
    };

    return (
        <div className="filter-panel">
            <h3>Filter by School Type</h3>
            {types.map((type) => (
                <div key={type}>
                    <input
                        type="checkbox"
                        id={type}
                        name={type}
                        value={type}
                        checked={selectedTypes.includes(type)}
                        onChange={() => handleCheckboxChange(type)}
                    />
                    <label htmlFor={type}>{type}</label>
                </div>
            ))}
        </div>
    );
};

export default TypeFilter;
