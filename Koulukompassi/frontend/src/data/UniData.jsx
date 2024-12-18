const UniData = async (name) => {
    try {
        const response = await fetch("http://localhost:8080/api/excel/university/"+name);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const result = await response.json();
        return result;
    } catch (error) {
        console.error("Error fetching coordinates for universities:", error);
        return null;
    }
};


export default UniData;
