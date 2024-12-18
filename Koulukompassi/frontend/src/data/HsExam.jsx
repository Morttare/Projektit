export const HsExam = async (school, year) => {
    try{
        const response=await fetch("http://localhost:8080/api/stats/"+
            school+"/"+year+"/examgrades");
        if (!response.ok){
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    }
    catch(error){
        console.error("Error loading exam statistics for high school: ",error);
        return null;
    }
}
