import React, {useEffect, useState} from "react";
import UniData from "../../data/UniData.jsx";
import {Button} from "react-bootstrap";
import AdmissionChart from "../../data/AdmissionDataGraph.jsx";
import {AdmissionData} from "../../data/AdmissionData.jsx";
import './SidePanel.css';
import HsChart from "../../data/HsDataGraph.jsx";
import HsStudentInfo from "../../data/HsStudentInfo.jsx";
import {HsExam} from "../../data/HsExam.jsx";

const SidePanel=({school, closePanel, type, isOpen, uniPoints, realUniPoints, selectedRealUniField, combinedSpecific}) =>{
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedField,setSelectedField]=useState(null);
    const [isFieldDataOpen,setIsFieldDataOpen]=useState(false);
    const [isGraphOpen, setIsGraphOpen] = useState(false);
    const [selectedMethod, setSelectedMethod] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [chartData, setChartData] = useState([]);
    const [selectedYear, setSelectedYear] = useState(2023);


    const toggleFieldData = (field) => {
        setSelectedField(field);
        setIsFieldDataOpen(!isFieldDataOpen);
    };

    const closeFieldData = () => {
        setIsFieldDataOpen(false);
        setSelectedField(null);
    }

    const showGraph = (method) => {
        setSelectedMethod(method);
        setIsGraphOpen(true);
    };

    const closeGraph = () => {
        setIsGraphOpen(false);
        setSelectedMethod(null);
    };

    const isGraphAvailable = async(method) => {
        try {
            const result = await AdmissionData(data.name, selectedField.name, method.name);
            return Object.keys(result).length!==1;
        } catch (error) {
            return false;
        }

    };

    const filterFields = (fields) => {
        const filteredFields = [];
        const filteredFields2 = new Set();
        const fieldsToDelete = new Set();

        if(selectedRealUniField === undefined && selectedField === null) {
            return fields;
        }

        if (uniPoints === 0 || type === "university" || school.includes("yliopisto")) {
            fields.forEach((field) => {
                let fieldName = field.name;
                combinedSpecific.forEach((data) => {
                    if (fieldName === data.fieldName) {
                        if (realUniPoints !== 0) {
                            for (const [key, value] of Object.entries(data)) {
                                if (key.includes("admissionMethods")) {
                                    value.forEach((method) => {
                                        const methodType = Object.values(method)[0];
                                        const methodPoints = parseFloat(Object.values(method)[1]);

                                        if (methodType === "Todistusvalinta" && methodPoints < realUniPoints) {
                                            filteredFields2.add(field.name);
                                        }
                                    });
                                }
                            }
                        } else {
                            filteredFields2.add(field.name);
                        }
                    }
                });
            });
        }
        if(type !== "university" && !school.includes("yliopisto" )) {
            fields.forEach((field) => {
                const methods = field.admissionMethods.map((method) => method.name);
                field.admissionMethods.forEach((method) => {
                    if (
                        (method.name === "Todistusvalinta (YO)" ||
                            (method.name === "Todistusvalinta" &&
                                methods.includes("Todistusvalinta (AMM)"))) &&
                        method.requiredPoints > uniPoints &&
                        uniPoints !== 0
                    ) {
                        fieldsToDelete.add(field.name);
                    }

                    if (
                        (!methods.includes("Todistusvalinta") &&
                            !methods.includes("Todistusvalinta (YO)") &&
                            uniPoints !== 0) ||
                        (methods.length === 0 && uniPoints !== 0)
                    ) {
                        fieldsToDelete.add(field.name);
                    }
                });
            });
        }

        fields.forEach((field) => {
            if(filteredFields2.size === 0) {
                if (!fieldsToDelete.has(field.name)) {
                    filteredFields.push(field);
                }
            }
            else {
                if (filteredFields2.has(field.name)) {
                    filteredFields.push(field);
                }
            }
        });

        return filteredFields;
    };



    function FieldData({field,closeFieldData}){
        const [availability, setAvailability] = useState({});

        useEffect(() => {
            const checkAvailability = async () => {
                const availabilityMap = {};
                await Promise.all(
                    (field.admissionMethods || []).map(async (method) => {
                        const available = await isGraphAvailable(method);
                        availabilityMap[method.name] = available;
                    })
                );
                setAvailability(availabilityMap);
            };

            checkAvailability();
        }, [field]);

        return (
            <div className={`field-data-${field.name || ''} ${isFieldDataOpen ? "field-data-open" : ""}`}>
                <button onClick={() => {
                    closeFieldData();
                    closeGraph();
                }} className="close-field-data-button">
                    Close
                </button>
                <h4>Admission Methods:</h4>
                <ul>
                    {field.admissionMethods?.map((method, methodIndex) => (
                        <li key={methodIndex}>
                            <p><strong>Method:</strong> {method.name || 'N/A'}</p>
                            <p><strong>Required Points:</strong> {method.requiredPoints || 'N/A'}</p>
                            {availability[method.name] ? (
                                <Button onClick={() => showGraph(method)}>View Chart</Button>
                            ) : (
                                <div></div>
                            )}
                        </li>
                    ))}
                </ul>
            </div>);
    }

    useEffect(() => {
        const fetchData = async () => {
            const result = await HsExam(school, selectedYear);
            let transformedData;
            if (result) {
                if (Object.keys(result).length===1){
                    transformedData=null
                    setChartData(transformedData);
                }
                else {
                    transformedData = Object.keys(result).map(grade => ({
                        grade: grade,
                        numberOfGrades: result[grade]
                    }));
                    setChartData(transformedData);

                }
            }
        };
        fetchData();
    }, [school, selectedYear]);


    const handleYearChange = (event) => {
        const newYear = Number(event.target.value);
        setSelectedYear(newYear);
    };

    if (type === "high_school") {
        return (
            <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
                <button onClick={() => closePanel()} className="close-panel-button">
                    &times; Close
                </button>
                <h2>{school}</h2>
                <HsStudentInfo school={school} year={selectedYear}/>
                <HsChart data={chartData}/>
                <div className="year-selector">
                    <label htmlFor="year">Select Year: </label>
                    <select
                        id="year"
                        value={selectedYear}
                        onChange={handleYearChange}
                    >
                        <option value={2017}>2017</option>
                        <option value={2018}>2018</option>
                        <option value={2019}>2019</option>
                        <option value={2020}>2020</option>
                        <option value={2021}>2021</option>
                        <option value={2022}>2022</option>
                        <option value={2023}>2023</option>
                        <option value={2024}>2024</option>
                    </select>
                </div>
            </div>
        );
    } else {
        useEffect(() => {
            const fetchSchoolData = async () => {
                try {
                    setLoading(true);
                    const result = await UniData(school);
                    const filteredData = {
                        ...result,
                        fields: filterFields(result.fields || []),
                    };
                    setData(filteredData);
                } catch (err) {
                    setError("Error fetching school data");
                } finally {
                    setLoading(false);
                }
            };

            if (school) fetchSchoolData();
        }, [school]);

        if (loading) return <div className="side-panel">Loading...</div>;
        if (error) return <div className="side-panel">Error: {error}</div>;

        if (isGraphOpen && data && selectedField && selectedMethod){
            return(
                <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
                    <button onClick={() => {
                        closePanel();
                        closeGraph();
                    }} className="close-panel-button">
                        &times; Close
                    </button>
                    <div className="graph-panel">
                        <h2>{data.name}</h2>
                        <h3>{selectedField.name}</h3>
                        <button onClick={closeGraph} className="close-graph-button">
                            &times; Close chart
                        </button>
                        <h4>{selectedMethod.name}</h4>
                        <AdmissionChart name={data.name} field={selectedField.name}
                                        admissionMethod={selectedMethod.name}/>
                    </div>
                </div>
            )
        }

        const filteredFields =
            data?.fields?.filter((field) =>
                field.name.toLowerCase().includes(searchTerm.toLowerCase())
            ) || [];
        return (
        <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
            <button onClick={() => {
                closePanel();
                closeGraph();
            }} className="close-panel-button">
                &times; Close
            </button>
            <h2>{school}</h2>
            {data && (
                <>
                        {selectedField === null ?
                            (
                                <div>
                                    <h3>Fields of Study:</h3>
                                    <input
                                        type="text"
                                        placeholder="Search fields of study"
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                        className="search-input"
                                    />
                                </div>
                            ) :
                            (
                                <p>{selectedField.name}</p>
                            )}
                    <div>
                        <ul>
                            {selectedField === null ? filteredFields?.map((field, fieldIndex) => (
                                    <li key={fieldIndex}>
                                            <Button onClick={() =>toggleFieldData(field)}>
                                                <p>{field.name}</p>
                                            </Button>
                                        </li>
                                    ))
                                    : (<li>
                                        <FieldData field={selectedField} closeFieldData={closeFieldData} />
                                    </li>)}
                            </ul>
                            {isGraphOpen && selectedMethod && (
                                <div className="graph-panel">
                                    <button onClick={closeGraph} className="close-graph-button">
                                        &times; Close chart
                                    </button>
                                    <AdmissionChart name={data.name} field={selectedField.name} admissionMethod={selectedMethod.name}/>
                                </div>
                            )}
                        </div>
                    </>
                )
                }
            </div>
        );
    }
}
export default SidePanel;