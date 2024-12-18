export const FieldData = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/combined');
        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }
        const data = await response.json();
        const uniqueFields = [...new Set(data.map(item => item.topField))].sort();
        return uniqueFields;
    } catch (err) {
        console.error('Error fetching fields:', err);
    }
}
