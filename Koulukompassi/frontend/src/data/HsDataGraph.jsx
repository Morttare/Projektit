import React, { useEffect, useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import { HsExam } from './HsExam.jsx';

const HsChart = ({ data }) => {

    const isDataEmpty = () => !data || data.length === 0;

    if (isDataEmpty()){
        return <p>No data available for the selected year.</p>;
    }
    else {

        return (
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={data} margin={{top: 20, right: 30, left: 20, bottom: 5}}>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="grade"/>
                    <YAxis/>
                    <Tooltip/>
                    <Legend/>
                    <Bar dataKey="numberOfGrades" fill="#8884d8" name="Grades"/>
                </BarChart>
            </ResponsiveContainer>
        );
    }
};

export default HsChart;
