<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Employee Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 20px;
        }
        h1 {
            text-align: center;
            color: #2c3e50;
        }
        .section {
            margin-top: 20px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
        }
        .section:last-child {
            border: none;
        }
        .details {
            display: flex;
            align-items: center;
        }
        .photo {
            width: 100px;
            height: 100px;
            margin-right: 20px;
            border: 1px solid #ccc;
            overflow: hidden;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        table th, table td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }
        .sub-section {
            margin-left: 20px;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <h1>Employee Details</h1>

    <!-- Employee Details -->
    <div class="section">
        <h2>Personal Information</h2>
        <div class="details">
            <#if photoUrl??>
            <div class="photo">
                <img src="${photoUrl}" alt="Photo" style="width:100%; height:100%;" />
            </div>
            </#if>
            <div>
                <p><strong>Name:</strong> ${employee.name?html}</p>
                <p><strong>Phone:</strong> ${employee.phoneNumber?html}</p>
                <p><strong>Email:</strong> ${employee.email?html}</p>
                <p><strong>Nature/Behavior:</strong> ${employee.natureBehavior?html}</p>
                <p><strong>Married:</strong> ${employee.married?string("Yes", "No")}</p>
            </div>
        </div>

        <!-- Spouse Details -->
        <#if employee.married>
        <div class="sub-section">
            <h3>Spouse Information</h3>
            <p><strong>Name:</strong> ${employee.spouse.name?html}</p>
            <p><strong>Age:</strong> ${employee.spouse.age}</p>
            <p><strong>Gender:</strong> ${employee.spouse.gender?html}</p>
            <p><strong>Occupation:</strong> ${employee.spouse.currentOccupation?html}</p>
        </div>
        </#if>

        <!-- Kids Details -->
        <#if employee.kids?has_content>
        <div class="sub-section">
            <h3>Kids Information</h3>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Age</th>
                        <th>Gender</th>
                        <th>Profession</th>
                    </tr>
                </thead>
                <tbody>
                    <#list employee.kids as kid>
                    <tr>
                        <td>${kid.name?html}</td>
                        <td>${kid.age}</td>
                        <td>${kid.gender?html}</td>
                        <td>${kid.profession?html}</td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </div>
        </#if>

        <p><strong>Extra Marital Affair:</strong> ${employee.extraMartialAffair?string("Yes", "No")}</p>
    </div>

    <!-- Professional Details -->
    <div class="section">
        <h2>Professional Information</h2>
        <h3>Current Employment</h3>
        <p><strong>Company:</strong> ${employee.professionalDetails.currentCompany?html}</p>
        <p><strong>Designation:</strong> ${employee.professionalDetails.currentDesignation?html}</p>
        <p><strong>Salary (Per Annum):</strong> ${employee.professionalDetails.currentSalary}</p>
        <p><strong>Location:</strong> ${employee.professionalDetails.currentLocation?html}</p>

        <h3>Past Employment</h3>
        <table>
            <thead>
                <tr>
                    <th>Company</th>
                    <th>Designation</th>
                    <th>Salary</th>
                </tr>
            </thead>
            <tbody>
                <#list employee.pastEmployments as job>
                <tr>
                    <td>${(job.companyName?html)}</td>
                    <td>${(job.designation?html)}</td>
                    <td>${(job.salary)}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- Dream/Wish -->
    <div class="section">
        <h2>Dream / Wish</h2>
        <p>${employee.dreamWish?html}</p>
    </div>
</body>
</html>
