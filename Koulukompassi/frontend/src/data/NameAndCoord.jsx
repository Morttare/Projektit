const Universities = async () => {
    try {
        const response = await fetch("http://localhost:8080/api/unicoordinates");
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

const Highschools = async () => {
    try {
        const response = await fetch("http://localhost:8080/api/hscoordinates");
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const result = await response.json();
        return result;
    } catch (error) {
        console.error("Error fetching coordinates for highschools:", error);
        return null;
    }
};

const getData = async (type) => {
    if (type === "college") {
        return await Universities();
    } else if (type === "high_school") {
        return await Highschools();
    } else {
        return null;
    }
};

export default getData;