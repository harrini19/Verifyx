import os

base_dir = r"C:\Users\sreeh\OneDrive\Desktop\verification\verification\src\main\resources\templates"
static_dir = r"C:\Users\sreeh\OneDrive\Desktop\verification\verification\src\main\resources\static"

os.makedirs(os.path.join(base_dir, "fragments"), exist_ok=True)
os.makedirs(os.path.join(base_dir, "reports"), exist_ok=True)
os.makedirs(os.path.join(base_dir, "billing"), exist_ok=True)
os.makedirs(os.path.join(base_dir, "certificates"), exist_ok=True)
os.makedirs(os.path.join(base_dir, "accounts"), exist_ok=True)
os.makedirs(os.path.join(static_dir, "css"), exist_ok=True)

css_content = """
:root {
    --primary-color: #2c3e50;
    --secondary-color: #34495e;
    --accent-color: #3498db;
    --success-color: #2ecc71;
    --danger-color: #e74c3c;
    --warning-color: #f1c40f;
    --light-bg: #f4f6f9;
    --text-color: #333;
    --white: #fff;
    --border-color: #ecf0f1;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 0;
    padding: 0;
    background-color: var(--light-bg);
    color: var(--text-color);
    display: flex;
    min-height: 100vh;
}

/* Sidebar */
.sidebar {
    width: 250px;
    background-color: var(--primary-color);
    color: var(--white);
    flex-shrink: 0;
    display: flex;
    flex-direction: column;
}

.sidebar-header {
    padding: 20px;
    background-color: var(--secondary-color);
    font-size: 1.2rem;
    font-weight: bold;
    text-align: center;
    border-bottom: 1px solid var(--primary-color);
}

.sidebar-menu {
    list-style: none;
    padding: 0;
    margin: 0;
    flex-grow: 1;
}

.sidebar-menu li {
    border-bottom: 1px solid var(--secondary-color);
}

.sidebar-menu a {
    display: block;
    padding: 15px 20px;
    color: var(--white);
    text-decoration: none;
    transition: background-color 0.3s;
}

.sidebar-menu a:hover {
    background-color: var(--accent-color);
}

/* Main Content */
.main-content {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
}

.topbar {
    background-color: var(--white);
    height: 60px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    display: flex;
    align-items: center;
    padding: 0 20px;
    justify-content: flex-end;
}

.content-area {
    padding: 20px;
    flex-grow: 1;
    overflow-y: auto;
}

/* Dashboard Cards */
.card-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
    margin-bottom: 20px;
}

.card {
    background-color: var(--white);
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    text-align: center;
}

.card h3 {
    margin: 0 0 10px 0;
    font-size: 1rem;
    color: var(--secondary-color);
}

.card .value {
    font-size: 1.8rem;
    font-weight: bold;
    color: var(--accent-color);
}

.card.success .value { color: var(--success-color); }
.card.danger .value { color: var(--danger-color); }
.card.warning .value { color: var(--warning-color); }

/* Tables */
table {
    width: 100%;
    border-collapse: collapse;
    background-color: var(--white);
    box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 20px;
}

th, td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid var(--border-color);
}

th {
    background-color: var(--secondary-color);
    color: var(--white);
}

tr:hover {
    background-color: #f9fafb;
}

/* Buttons */
.btn {
    display: inline-block;
    padding: 8px 15px;
    border-radius: 4px;
    text-decoration: none;
    color: var(--white);
    background-color: var(--accent-color);
    border: none;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s;
}

.btn:hover { background-color: #2980b9; }
.btn-success { background-color: var(--success-color); }
.btn-success:hover { background-color: #27ae60; }
.btn-danger { background-color: var(--danger-color); }
.btn-danger:hover { background-color: #c0392b; }
.btn-warning { background-color: var(--warning-color); }
.btn-warning:hover { background-color: #f39c12; }
.btn-info { background-color: var(--accent-color); }

/* Forms */
.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

.form-control {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}

.form-container {
    background: var(--white);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    max-width: 600px;
    margin: 0 auto;
}

/* Badges */
.badge {
    padding: 4px 8px;
    border-radius: 12px;
    font-size: 12px;
    color: var(--white);
}
.badge-success { background-color: var(--success-color); }
.badge-warning { background-color: var(--warning-color); }
.badge-danger { background-color: var(--danger-color); }
.badge-info { background-color: var(--accent-color); }

.alert {
    padding: 15px;
    margin-bottom: 20px;
    border-radius: 4px;
}
.alert-success { background-color: #d4edda; color: #155724; }
.alert-danger { background-color: #f8d7da; color: #721c24; }
"""

with open(os.path.join(static_dir, "css", "style.css"), "w") as f:
    f.write(css_content)

layout_html = """<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="head(title)">
    <meta charset="UTF-8">
    <title th:text="${title} + ' - Credential Platform'">Platform</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <div class="sidebar" th:fragment="sidebar">
        <div class="sidebar-header">
            Credential Platform
        </div>
        <ul class="sidebar-menu">
            <li><a th:href="@{/}">DASHBOARD</a></li>
            <li><strong style="padding: 10px 20px; display:block; color:#aaa; font-size:12px;">CERTIFICATES</strong></li>
            <li><a th:href="@{/certificates/issue}">Issue Certificate</a></li>
            <li><a th:href="@{/certificates}">All Certificates</a></li>
            <li><a th:href="@{/certificates/verify}">Verify Certificate</a></li>
            <li><strong style="padding: 10px 20px; display:block; color:#aaa; font-size:12px;">FINANCE</strong></li>
            <li><a th:href="@{/accounts}">Chart of Accounts</a></li>
            <li><a th:href="@{/billing/invoices}">Invoices</a></li>
            <li><a th:href="@{/billing/payments}">Payments</a></li>
            <li><strong style="padding: 10px 20px; display:block; color:#aaa; font-size:12px;">REPORTS</strong></li>
            <li><a th:href="@{/reports/pnl}">Platform P&L</a></li>
            <li><a th:href="@{/reports/budget}">Unit Budget</a></li>
        </ul>
    </div>
    
    <div class="main-content" th:fragment="topbar">
        <div class="topbar">
            <span>Admin User</span>
        </div>
    </div>
</body>
</html>
"""

with open(os.path.join(base_dir, "fragments", "layout.html"), "w") as f:
    f.write(layout_html)

dashboard_html = """<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head('Dashboard')}"></head>
<body>
    <div th:replace="~{fragments/layout :: sidebar}"></div>
    <div class="main-content">
        <div th:replace="~{fragments/layout :: topbar}"></div>
        <div class="content-area">
            <h2>Dashboard</h2>
            <div class="card-container">
                <div class="card">
                    <h3>Total Certificates</h3>
                    <div class="value" th:text="${stats.totalCertificates}">0</div>
                </div>
                <div class="card success">
                    <h3>Verified Certificates</h3>
                    <div class="value" th:text="${stats.verifiedCertificates}">0</div>
                </div>
                <div class="card warning">
                    <h3>Pending Invoices</h3>
                    <div class="value" th:text="${stats.pendingInvoices}">0</div>
                </div>
                <div class="card success">
                    <h3>Total Revenue</h3>
                    <div class="value" th:text="${'$' + stats.totalRevenue}">$0.00</div>
                </div>
                <div class="card danger">
                    <h3>Total Expenses</h3>
                    <div class="value" th:text="${'$' + stats.totalExpenses}">$0.00</div>
                </div>
                <div class="card" th:classappend="${stats.profitable} ? 'success' : 'danger'">
                    <h3>Net Profit/Loss</h3>
                    <div class="value" th:text="${'$' + stats.netProfitLoss}">$0.00</div>
                </div>
            </div>

            <div class="card-container" style="grid-template-columns: 1fr 1fr 1fr;">
                <div>
                    <h3>Recent Certificates</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Recipient</th>
                                <th>Course</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr th:each="cert : ${stats.recentCertificates}">
                                <td th:text="${cert.studentName}">Name</td>
                                <td th:text="${cert.course}">Course</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div>
                    <h3>Recent Invoices</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Client</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr th:each="inv : ${stats.recentInvoices}">
                                <td th:text="${inv.clientName}">Client</td>
                                <td><span class="badge" th:classappend="${inv.status.name() == 'PAID'} ? 'badge-success' : 'badge-warning'" th:text="${inv.status}">STATUS</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div>
                    <h3>Recent Payments</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Amount</th>
                                <th>Method</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr th:each="pay : ${stats.recentPayments}">
                                <td th:text="${'$' + pay.amount}">$0.00</td>
                                <td th:text="${pay.paymentMethod}">Method</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
"""

with open(os.path.join(base_dir, "dashboard.html"), "w") as f:
    f.write(dashboard_html)

# Add other templates as basic placeholders that extend the layout

def create_template(path, title, content):
    full_path = os.path.join(base_dir, path)
    html = f"""<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{{fragments/layout :: head('{title}')}}"></head>
<body>
    <div th:replace="~{{fragments/layout :: sidebar}}"></div>
    <div class="main-content">
        <div th:replace="~{{fragments/layout :: topbar}}"></div>
        <div class="content-area">
            <h2>{title}</h2>
            {content}
        </div>
    </div>
</body>
</html>"""
    with open(full_path, "w") as f:
        f.write(html)

create_template("reports/pnl.html", "Platform P&L", """
<table>
    <thead>
        <tr>
            <th>Type</th>
            <th>Amount</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Total Revenue</td>
            <td th:text="${'$' + report.totalRevenue}">0.00</td>
        </tr>
        <tr>
            <td>Total Expenses</td>
            <td th:text="${'$' + report.totalExpenses}">0.00</td>
        </tr>
        <tr style="font-weight: bold;">
            <td>Net Profit/Loss</td>
            <td th:text="${'$' + report.netProfitLoss}" th:style="${report.profitable} ? 'color: var(--success-color)' : 'color: var(--danger-color)'">0.00</td>
        </tr>
    </tbody>
</table>
""")

create_template("reports/budget.html", "Unit Budget", """
<table>
    <thead>
        <tr>
            <th>Unit</th>
            <th>Allocated Budget</th>
            <th>Actual Spending</th>
            <th>Remaining Budget</th>
            <th>Utilization %</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="item : ${report.unitBudgets}">
            <td th:text="${item.unitName}">Unit</td>
            <td th:text="${'$' + item.allocatedBudget}">$0.00</td>
            <td th:text="${'$' + item.actualSpending}">$0.00</td>
            <td th:text="${'$' + item.remainingBudget}">$0.00</td>
            <td th:text="${item.utilizationPercentage + '%'}">0%</td>
        </tr>
    </tbody>
</table>
""")

create_template("billing/dashboard.html", "Billing Dashboard", """
<div class="card-container">
    <div class="card">
        <h3>Go to Invoices</h3>
        <a th:href="@{/billing/invoices}" class="btn btn-info">Invoices</a>
    </div>
    <div class="card">
        <h3>Go to Payments</h3>
        <a th:href="@{/billing/payments}" class="btn btn-info">Payments</a>
    </div>
</div>
""")

create_template("billing/invoices.html", "Invoices", """
<a th:href="@{/billing/invoices/new}" class="btn btn-success" style="margin-bottom:15px;">Create Invoice</a>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Client Name</th>
            <th>Amount</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="inv : ${invoices}">
            <td th:text="${inv.id}">1</td>
            <td th:text="${inv.clientName}">Client</td>
            <td th:text="${'$' + inv.amount}">0.00</td>
            <td><span class="badge" th:classappend="${inv.status.name() == 'PAID'} ? 'badge-success' : 'badge-warning'" th:text="${inv.status}">STATUS</span></td>
        </tr>
    </tbody>
</table>
""")

create_template("billing/invoice-form.html", "Create Invoice", """
<div class="form-container">
    <form th:action="@{/billing/invoices}" th:object="${invoice}" method="post">
        <div class="form-group">
            <label>Client Name</label>
            <input type="text" th:field="*{clientName}" class="form-control" required/>
            <span th:if="${#fields.hasErrors('clientName')}" th:errors="*{clientName}" style="color:red;"></span>
        </div>
        <div class="form-group">
            <label>Amount</label>
            <input type="number" step="0.01" th:field="*{amount}" class="form-control" required/>
            <span th:if="${#fields.hasErrors('amount')}" th:errors="*{amount}" style="color:red;"></span>
        </div>
        <button type="submit" class="btn btn-success">Save Invoice</button>
    </form>
</div>
""")

create_template("billing/payments.html", "Payments", """
<a th:href="@{/billing/payments/new}" class="btn btn-success" style="margin-bottom:15px;">Register Payment</a>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Invoice ID</th>
            <th>Amount</th>
            <th>Payment Method</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="pay : ${payments}">
            <td th:text="${pay.id}">1</td>
            <td th:text="${pay.invoice != null ? pay.invoice.id : ''}">1</td>
            <td th:text="${'$' + pay.amount}">0.00</td>
            <td th:text="${pay.paymentMethod}">Method</td>
        </tr>
    </tbody>
</table>
""")

create_template("billing/payment-form.html", "Register Payment", """
<div class="form-container">
    <form th:action="@{/billing/payments}" th:object="${payment}" method="post">
        <div class="form-group">
            <label>Invoice ID</label>
            <select name="invoice.id" class="form-control" required>
                <option th:each="inv : ${invoices}" th:value="${inv.id}" th:text="${'INV-' + inv.id + ' (' + inv.clientName + ' - $' + inv.amount + ')'}"></option>
            </select>
        </div>
        <div class="form-group">
            <label>Amount</label>
            <input type="number" step="0.01" th:field="*{amount}" class="form-control" required/>
        </div>
        <div class="form-group">
            <label>Payment Method</label>
            <input type="text" th:field="*{paymentMethod}" class="form-control" required/>
        </div>
        <button type="submit" class="btn btn-success">Register Payment</button>
    </form>
</div>
""")

create_template("certificates/list.html", "Certificates", """
<a th:href="@{/certificates/issue}" class="btn btn-success" style="margin-bottom:15px;">Issue New Certificate</a>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Recipient</th>
            <th>Course</th>
            <th>Grade</th>
            <th>Issue Date</th>
            <th>Valid</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="cert : ${certificates}">
            <td th:text="${cert.id}">1</td>
            <td th:text="${cert.studentName}">Name</td>
            <td th:text="${cert.course}">Course</td>
            <td th:text="${cert.grade}">A</td>
            <td th:text="${cert.issueDate}">Date</td>
            <td><span class="badge" th:classappend="${cert.valid} ? 'badge-success' : 'badge-danger'" th:text="${cert.valid ? 'Yes' : 'No'}">Yes</span></td>
        </tr>
    </tbody>
</table>
""")

create_template("certificates/issue.html", "Issue Certificate", """
<div class="form-container">
    <form th:action="@{/certificates/issue}" th:object="${certificate}" method="post">
        <div class="form-group">
            <label>Recipient Name</label>
            <input type="text" th:field="*{studentName}" class="form-control" required/>
        </div>
        <div class="form-group">
            <label>Course Name</label>
            <input type="text" th:field="*{course}" class="form-control" required/>
        </div>
        <div class="form-group">
            <label>Grade</label>
            <input type="text" th:field="*{grade}" class="form-control" required/>
        </div>
        <button type="submit" class="btn btn-success">Issue Certificate</button>
    </form>
</div>
""")

create_template("certificates/issued.html", "Certificate Issued", """
<div class="alert alert-success">
    <strong>Success!</strong> Certificate has been issued.
</div>
<div class="card" style="text-align: left;" th:if="${certificate != null}">
    <h3>Certificate Details</h3>
    <p><strong>Recipient:</strong> <span th:text="${certificate.studentName}"></span></p>
    <p><strong>Course:</strong> <span th:text="${certificate.course}"></span></p>
    <p><strong>Grade:</strong> <span th:text="${certificate.grade}"></span></p>
    <div style="background:#eee; padding:15px; margin-top:20px; border-radius:4px; word-break: break-all;">
        <strong>SHA-256 Hash:</strong>
        <p th:text="${certificate.hashValue}" style="font-family: monospace; font-size: 16px; margin-bottom: 0; color: #2c3e50; font-weight: bold; user-select: all;"></p>
    </div>
    <a th:href="@{/certificates}" class="btn btn-info" style="margin-top:15px;">Back to Certificates</a>
</div>
""")

create_template("certificates/verify.html", "Verify Certificate", """
<div class="form-container" style="margin-bottom: 20px;">
    <form th:action="@{/certificates/verify}" method="post">
        <div class="form-group">
            <label>Enter SHA-256 Hash to Verify</label>
            <input type="text" name="hash" class="form-control" placeholder="e.g. 5e884898da..." required/>
        </div>
        <button type="submit" class="btn btn-info">Verify</button>
    </form>
</div>

<div th:if="${verified != null}">
    <div th:if="${verified}" class="alert alert-success">
        <strong style="font-size: 1.5rem;">VERIFIED</strong>
    </div>
    <div th:if="${!verified}" class="alert alert-danger">
        <strong style="font-size: 1.5rem;">NOT VERIFIED</strong>
    </div>
    
    <div th:if="${certificate != null}" class="card" style="text-align: left;">
        <h3>Certificate Details</h3>
        <p><strong>Recipient:</strong> <span th:text="${certificate.studentName}"></span></p>
        <p><strong>Course:</strong> <span th:text="${certificate.course}"></span></p>
        <p><strong>Grade:</strong> <span th:text="${certificate.grade}"></span></p>
        <p><strong>Issue Date:</strong> <span th:text="${certificate.issueDate}"></span></p>
    </div>
</div>
""")

create_template("accounts/list.html", "Accounts", """
<a th:href="@{/accounts/new}" class="btn btn-success" style="margin-bottom:15px;">Add Account</a>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Balance</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="acc : ${accounts}">
            <td th:text="${acc.id}">1</td>
            <td th:text="${acc.name}">Name</td>
            <td th:text="${acc.type}">Type</td>
            <td th:text="${'$' + acc.balance}">0.00</td>
            <td>
                <a th:href="@{/accounts/edit/{id}(id=${acc.id})}" class="btn btn-info">Edit</a>
            </td>
        </tr>
    </tbody>
</table>
""")

create_template("accounts/form.html", "Account Form", """
<div class="form-container">
    <form th:action="@{/accounts}" th:object="${account}" method="post">
        <input type="hidden" th:field="*{id}" />
        <div class="form-group">
            <label>Name</label>
            <input type="text" th:field="*{name}" class="form-control" required/>
        </div>
        <div class="form-group">
            <label>Type</label>
            <select th:field="*{type}" class="form-control">
                <option value="ASSET">ASSET</option>
                <option value="LIABILITY">LIABILITY</option>
                <option value="EQUITY">EQUITY</option>
                <option value="REVENUE">REVENUE</option>
                <option value="EXPENSE">EXPENSE</option>
            </select>
        </div>
        <div class="form-group">
            <label>Balance</label>
            <input type="number" step="0.01" th:field="*{balance}" class="form-control" required/>
        </div>
        <button type="submit" class="btn btn-success">Save</button>
    </form>
</div>
""")

print("Successfully created templates!")
