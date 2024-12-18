export const AdmissionData = async (name,field,admissionMethod) => {
    try{
        const response=await fetch("http://localhost:8080/api/excel/graph/"+
            name+"/"+field+"/"+admissionMethod);
        if (!response.ok){
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    }
    catch(error){
        console.error("Error loading admission data for university and field: ",error);
        return null;
    }
}
