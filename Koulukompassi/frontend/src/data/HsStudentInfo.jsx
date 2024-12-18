import React, { useEffect, useState } from 'react';
import { HsStudent } from './HsStudent.jsx';




const HsStudentInfo = ({ school, year }) => {
    const [data, setData] = useState({studentCount: "", averageGrade: 0.0});

    const isDataEmpty = () => !data || data.length === 0;

    useEffect(() => {
        const fetchData = async () => {
            const result = await HsStudent(school, year);
            if (result) {

                if (Object.keys(result).length===1){

                    const data = Object.entries(result)
                    setData({studentCount: data[0][0], averageGrade: data[0][1]});
                }

            }
        };
        fetchData();
    }, [school, year]);
    if (isDataEmpty()){
        return null;
    }
    else {

        return (
            <div className="student-info">
                <p>Number of Students: {data.studentCount}</p>
                <p>Average Grade: {data.averageGrade}</p>
            </div>
        );
    }
};

export default HsStudentInfo;

