package technology.innovate.haziremployee.rest.entity.joblistdetail

class JobdetailDataModel {

    private  var jobpostname: String? = null
    private   var jobcategory: String? = null
    private  var minexperience: String? = null
    private  var maxexperience: String? = null
    private  var jobtype: String? = null
    private  var departmentname: String? = null
    private  var companyname: String? = null
    private  var joblocation: String? = null
    private  var status: String? = null
    private  var designationname: String? = null
    private  var branchname: String? = null
    private  var minsalary: String? = null
    private  var maxsalary: String? = null
    private  var isdisplaysalaryjobpage: String? = null
    private  var jobdescription: String? = null
    private  var noofpositions: String? = null
    private  var jobresponsibilities: String? = null
    private  var noofrounds: String? = null
    private  var country: String? = null
    private  var currency: String? = null



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


    fun getDesignationname(): String? {
        return designationname.toString()
    }

    fun setDesignationname(designationname: String?) {
        this.designationname = designationname
    }


    fun getBranchname(): String? {
        return branchname.toString()
    }

    fun setBranchname(branchname: String?) {
        this.branchname = branchname
    }


    fun getMinsalary(): String? {
        return minsalary.toString()
    }

    fun setMinsalary(minsalary: String?) {
        this.minsalary = minsalary
    }


    fun getMaxsalary(): String? {
        return maxsalary.toString()
    }

    fun setMaxsalary(maxsalary: String?) {
        this.maxsalary = maxsalary
    }

    fun getIsdisplaysalaryjob_page(): String? {
        return isdisplaysalaryjobpage.toString()
    }

    fun setIsdisplaysalaryjobpage(isdisplaysalaryjobpage: String?) {
        this.isdisplaysalaryjobpage = isdisplaysalaryjobpage
    }



    fun getJobdescription(): String? {
        return jobdescription.toString()
    }

    fun setJobdescription(jobdescription: String?) {
        this.jobdescription = jobdescription
    }


    fun getNoofpositions(): String? {
        return noofpositions.toString()
    }

    fun setNoofpositions(noofpositions: String?) {
        this.noofpositions = noofpositions
    }


    fun getJobresponsibilities(): String? {
        return jobresponsibilities.toString()
    }

    fun setJobresponsibilities(jobresponsibilities: String?) {
        this.jobresponsibilities = jobresponsibilities
    }


    fun getNoofrounds(): String? {
        return noofrounds.toString()
    }

    fun setNoofrounds(noofrounds: String?) {
        this.noofrounds = noofrounds
    }

    fun getCountry(): String? {
        return country.toString()
    }

    fun setCountry(country: String?) {
        this.country = country
    }

    fun getCurrency(): String? {
        return currency.toString()
    }

    fun setCurrency(currency: String?) {
        this.currency = currency
    }
}