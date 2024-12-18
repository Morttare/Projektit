import React, {useEffect} from 'react';
import getData from '../../data/NameAndCoord.jsx';

const SearchBar = ({ type, togglePanel }) => {
    const [searchQuery, setSearchQuery] = React.useState("");
    const [filteredItems, setFilteredItems] = React.useState([]);
    const [schoolNames, setSchoolNames] = React.useState([]);
    const [focus, setFocus] = React.useState(false);

    useEffect(() => {
        const fetchData = async () => {
            const data = await getData(type);
            let schoolNamesList = Object.keys(data);
            setSchoolNames(schoolNamesList);
            setFilteredItems(schoolNamesList);
        };
        fetchData();
    }, [type]);

    const handleSearch = (search) => {
        const query = search.target.value;
        setSearchQuery(query);

        const filtered = schoolNames.filter(item => item.toLowerCase().includes(query.toLowerCase()));
        setFilteredItems(filtered);
    }

    const handleBlur = () => {
        setFocus(false);
    }

    return (
        <div>
            <input
                type="text"
                placeholder="Search..."
                value={searchQuery}
                onChange={handleSearch}
                onFocus={() => setFocus(true)}
                onBlur={handleBlur}
                style={{padding: "8px", width: "30%", marginBottom: "4px"}}
            />
            {focus && searchQuery && (
                <div
                    style={{
                        position: 'absolute',
                        top: '100%',
                        width: '225px',
                        maxHeight: '300px',
                        overflowY: 'auto',
                        backgroundColor: 'white',
                        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)',
                        zIndex: 1000,
                    }}
                >
                    <ul style={{listStyle: 'none', padding: 0, margin: 0}}>
                        {filteredItems.map((item, index) => (
                            <li
                                key={index}
                                style={{
                                    padding: '10px',
                                    borderBottom: '1px solid #ccc',
                                    cursor: 'pointer',
                                }}
                                onMouseDown={() => {
                                    setSearchQuery(item);
                                    setFocus(false);
                                    togglePanel(item, true);
                                }}
                            >
                                {item}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default SearchBar;