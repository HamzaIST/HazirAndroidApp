package technology.innovate.haziremployee.rest.entity.jobtitle

class DataModel {

    private  var jobpostname: String? = null
    private   var jobcategory: String? = null
    private  var minexperience: String? = null
    private  var maxexperience: String? = null
    private  var jobtype: String? = null
    private  var departmentname: String? = null
    private  var companyname: String? = null
    private  var joblocation: String? = null
    private  var status: String? = null
    private  var detailid: String? = null


    fun getPostname(): String {
        return jobpostname.toString()
    }

    fun setPostname(jobpostname: String?) {
        this.jobpostname = jobpostname
    }


    fun getJobcategory(): String?{
        return jobcategory.toString()
    }

    fun setJobcategory(jobcategory: String?) {
        this.jobcategory = jobcategory
    }



    fun getMinexperience(): String? {
        return minexperience.toString()
    }

    fun setMinexperience(minexperience: String?) {
        this.minexperience = minexperience
    }




    fun getMaxexperience(): String?{
        return maxexperience.toString()
    }

    fun setMaxexperience(maxexperience: String?) {
        this.maxexperience = maxexperience
    }



    fun getJobtype(): String ?{
        return jobtype.toString()
    }

    fun setJobtype(jobtype: String?) {
        this.jobtype = jobtype
    }


    fun getDepartmentname(): String? {
        return departmentname.toString()
    }

    fun setDepartmentname(departmentname: String?) {
        this.departmentname = departmentname
    }



    fun getCompanyname(): String? {
        return companyname.toString()
    }

    fun setCompanyname(companyname: String?) {
        this.companyname = companyname
    }



    fun getJoblocation(): String? {
        return joblocation.toString()
    }

    fun setJoblocation(joblocation: String?) {
        this.joblocation = joblocation
    }


    fun getStatus(): String? {
        return status.toString()
    }

    fun setStatus(status: String?) {
        this.status = status
    }


    fun getDetailid(): String? {
        return detailid.toString()
    }

    fun setDetailid(detailid: String?) {
        this.detailid = detailid
    }



}