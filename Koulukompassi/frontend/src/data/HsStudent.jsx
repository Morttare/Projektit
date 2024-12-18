export const HsStudent = async (school, year) => {
    try{
        const response=await fetch("http://localhost:8080/api/stats/"+
            school+"/"+year+"/studentgrades");
        if (!response.ok){
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    }
    catch(error){
        console.error("Error loading student statistics for high school: ",error);
        return null;
    }
}
