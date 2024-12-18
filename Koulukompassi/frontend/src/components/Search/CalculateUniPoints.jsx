
export const calculateRealUniPoints = (setTotalPointsForRealUniversity, combinedData, selectedGrades) => {

        const getSubjectPoints = (key, subject, data, selected_grade) => {
            if(key.toLowerCase() === subject) {
                for (const [grade, points] of Object.entries(data)) {
                    if(grade === selected_grade) {
                        let added_points = parseFloat(points);
                        if(subject.includes("matematiikka")) {
                            if(added_points > bestMathScore) {
                                bestMathScore = added_points;
                            }
                            continue;
                        }
                        if(subject.includes("kieli")) {
                            if(added_points > bestLanguageScore) {
                                bestLanguageScore = added_points;
                            }
                            continue;
                        }
                        totalPoints += added_points || 0;
                        console.log(`Points for ${subject} with grade ${grade}:`, points);
                        return added_points || 0;
                    }
                }
            }
        }

        function getTopXNumbers(numbers, x) {
            const sortedNumbers = [...numbers].sort((a, b) => b - a);
            const topXNumbers = sortedNumbers.slice(0, x);
            const sum = topXNumbers.reduce((accumulator, current) => accumulator + current, 0);
            const parseSum = parseFloat(sum);
            return parseSum;
        }

        let totalPoints = 0;
        let bestMathScore = 0;
        let bestLanguageScore = 0;
        let bestOrPoints = 0;
        let bestNumbersByBestOf = 0;

        let how_many_most = 0;

        let bestOfPoints = [];


        let pointSystem = { ...combinedData };
        const grades = { ...selectedGrades };

        for (const [key, selected_grade] of Object.entries(grades)) {
            for(let i = 0; i < Object.keys(pointSystem).length; i++) {
                const fieldsPoints = pointSystem[i].fieldsPoints;
                const how_many = pointSystem[i].bestOf;
                if(how_many > how_many_most) {
                    how_many_most = how_many;
                }
                let subjectsCount = Object.keys(fieldsPoints).length
                for (const [subject, data] of Object.entries(fieldsPoints)) {
                    if(subject.includes(" tai ")) {
                        const or_subjects = subject.split(" tai ");

                        if(or_subjects.length > 1) {
                            or_subjects.forEach((new_subject) => {
                                let or_points = parseFloat(getSubjectPoints(key, new_subject, data, selected_grade));
                                if(or_points) {
                                    totalPoints = totalPoints - or_points;
                                }
                                if(or_points > bestOrPoints) {
                                    bestOrPoints = or_points;
                                }
                            });
                        }
                    }
                    let got_points = getSubjectPoints(key, subject, data, selected_grade);
                    if(how_many >= 2 && !isNaN(got_points) && subjectsCount > how_many) {
                        bestOfPoints.push(got_points);
                    }
                }
            }
        }
        totalPoints = totalPoints - getTopXNumbers(bestOfPoints, bestOfPoints.length)
        let best_numbers = getTopXNumbers(bestOfPoints, how_many_most);
        if(!isNaN(best_numbers)) {
            bestNumbersByBestOf += best_numbers;
        }

        totalPoints += bestMathScore;
        totalPoints += bestLanguageScore;
        totalPoints += bestOrPoints;
        totalPoints += bestNumbersByBestOf;

        if(totalPoints < 0) totalPoints = 0;

        setTotalPointsForRealUniversity(totalPoints.toFixed(1));
    };