import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import { subjects } from "../../data/SubjectList.jsx";
import {calculateRealUniPoints} from "./CalculateUniPoints.jsx";

const GradeFilter = ({ setTotalPointsForUniversity, setTotalPointsForRealUniversity, combinedData, togglePopup, selectedGrades, setSelectedGrades }) => {

    const gradeOptions = [
        { value: '', label: '-' },
        { value: 'L', label: 'L' },
        { value: 'E', label: 'E' },
        { value: 'M', label: 'M' },
        { value: 'C', label: 'C' },
        { value: 'B', label: 'B' },
        { value: 'A', label: 'A' },
        { value: 'I', label: 'I' },
    ];

    const gradePointsMap = {
        'Äidinkieli': { L: 46, E: 41, M: 34, C: 26, B: 18, A: 10 },
        'Matematiikka, pitkä': { L: 46, E: 43, M: 40, C: 35, B: 27, A: 19 },
        'Matematiikka, lyhyt': { L: 40, E: 35, M: 27, C: 19, B: 13, A: 6 },
        'Kieli, pitkä': { L: 46, E: 41, M: 34, C: 26, B: 18, A: 10 },
        'Kieli, keskipitkä': { L: 38, E: 34, M: 26, C: 18, B: 12, A: 5 },
        'Kieli, lyhyt': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Fysiikka': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Kemia': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Biologia': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Maantiede': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Terveystieto': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Psykologia': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Filosofia': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Historia': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Yhteiskuntaoppi': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Uskonto': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
        'Elämänkatsomusoppi': { L: 30, E: 27, M: 21, C: 15, B: 9, A: 3 },
    };

    const calculateTotalPoints = () => {
        let totalPoints = 0;

        const grades = { ...selectedGrades };

        const motherTongueSubjects = ['Äidinkieli'];
        for (const subject of motherTongueSubjects) {
            const grade = grades[subject];
            if (grade && gradePointsMap[subject] && gradePointsMap[subject][grade]) {
                totalPoints += gradePointsMap[subject][grade];
            }
        }
        delete grades['Äidinkieli'];

        const reaaliaineet = [
            'Fysiikka',
            'Kemia',
            'Biologia',
            'Maantiede',
            'Terveystieto',
            'Psykologia',
            'Filosofia',
            'Historia',
            'Yhteiskuntaoppi',
            'Uskonto',
            'Elämänkatsomusoppi',
            'Kieli, pitkä',
            'Kieli, keskipitkä',
            'Kieli, lyhyt',
        ];
        const math = ['Matematiikka, pitkä', 'Matematiikka, lyhyt'];
        for (const mathType of math) {
            const grade = grades[mathType];
            if (grade && gradePointsMap[mathType] && gradePointsMap[mathType][grade]) {
                totalPoints += gradePointsMap[mathType][grade];
                break;
            }
        }
        math.forEach(mathType => delete grades[mathType]);
        const foreignLanguages = ['Kieli, pitkä', 'Kieli, keskipitkä', 'Kieli, lyhyt'];
        for (const language of foreignLanguages) {
            const grade = grades[language];
            if (grade && gradePointsMap[language] && gradePointsMap[language][grade]) {
                totalPoints += gradePointsMap[language][grade];
                delete grades[language];
                break;
            }
        }

        const reaaliPoints = reaaliaineet
            .map(subject => {
                const grade = grades[subject];
                return grade && gradePointsMap[subject] && gradePointsMap[subject][grade]
                    ? gradePointsMap[subject][grade]
                    : 0;
            })
            .sort((a, b) => b - a);
        totalPoints += (reaaliPoints[0] || 0) + (reaaliPoints[1] || 0);
        reaaliaineet.forEach(subject => delete grades[subject]);

        if(totalPoints >= 198) {
            totalPoints = 198;
        }

        setTotalPointsForUniversity(totalPoints);
    };


    const handleGradeChange = (subject, value) => {
        setSelectedGrades(prevGrades => ({
            ...prevGrades,
            [subject]: value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        calculateTotalPoints();
        calculateRealUniPoints(setTotalPointsForRealUniversity, combinedData, selectedGrades);
        const submissionData = {
            grades: selectedGrades,
        };

        togglePopup();
    };

    const handleReset = (e) => {
        e.preventDefault();

        const resetGrades = subjects.reduce((acc, subject) => {
            acc[subject] = '';
            return acc;
        }, {});
        setSelectedGrades(resetGrades);
    }

    return (
        <div>
            <h2>Grades</h2>
            <form onSubmit={handleSubmit}>
                {subjects.map(subject => (
                    <div key={subject} style={{marginBottom: '10px'}}>
                        <label htmlFor={subject} style={{marginRight: '10px'}}>
                            {subject}:
                        </label>
                        <select
                            id={subject}
                            value={selectedGrades[subject]}
                            onChange={(e) => handleGradeChange(subject, e.target.value)}
                        >
                            {gradeOptions.map(option => (
                                <option
                                    key={option.value || `${subject}-empty`}
                                    value={option.value}
                                >
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>
                ))}
                <Button
                    variant="primary"
                    onClick={handleReset}
                    size="lg">
                    Reset
                </Button>
                <button type="submit">Submit</button>
            </form>

        </div>
    );
};

export default GradeFilter;
