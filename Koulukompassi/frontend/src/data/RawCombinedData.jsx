export const RawCombinedData = async (field) => {
    try {
        const response = await fetch('http://localhost:8080/api/combined');

        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
            throw new Error('Data fetched is not an array');
        }

        const filteredData = data.filter(item => item.topField === field);

        return filteredData;
    } catch (err) {
        console.error('Error fetching fields:', err);
        return [];
    }
}