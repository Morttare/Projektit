import stringSimilarity, {compareTwoStrings} from 'string-similarity';

export const CombinedData = async (topField) => {
    try {
        const response = await fetch('http://localhost:8080/api/combined');
        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }
        const data = await response.json();
        let combinedData = [];
        try {
            if(topField === "Lääketieteelliset alat") {
                topField = "Biolääketieteet";
            }
            data.forEach(function(university, index) {
                if(compareTwoStrings(university.topField, topField) < 0.6) return;
                university.universityPoints.forEach(function(pointEntry) {
                    let fieldsPoints = pointEntry;
                    combinedData.push(fieldsPoints);
                });
                throw new Error('');
            });
        }
        catch {
            return combinedData
        }

    } catch (err) {
        console.error('Error fetching fields:', err);
    }
}