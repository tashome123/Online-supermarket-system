// ====== DOM Elements ======
const loginSection = document.getElementById('login-section');
const registerSection = document.getElementById('register-section');
const appSection = document.getElementById('app-section');
const showRegisterLink = document.getElementById('show-register');
const showLoginLink = document.getElementById('show-login');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const logoutBtn = document.getElementById('logout-btn');
const userName = document.getElementById('user-name');
const incomeAmount = document.getElementById('income-amount');
const expenseAmount = document.getElementById('expense-amount');
const balanceAmount = document.getElementById('balance');
const transactionForm = document.getElementById('transaction-form');
const transactionList = document.getElementById('transaction-list');
const addCategoryBtn = document.getElementById('add-category-btn');
const addGoalBtn = document.getElementById('add-goal-btn');
const savingsGoalsContainer = document.getElementById('savings-goals');
const categorySelect = document.getElementById('category');

// ====== Global Variables ======
let currentUser = null;
let budgetChart = null;

// ====== Chart Functions ======
function initializeChart() {
    const ctx = document.getElementById('budgetChart').getContext('2d');
    budgetChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Income', 'Expenses'],
            datasets: [{
                data: [0, 0],
                backgroundColor: ['#27ae60', '#e74c3c'],
                borderColor: ['#1e8449', '#c0392b'],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom' },
                title: { display: true, text: 'Income vs Expenses' }
            }
        }
    });
}

function updateChart(income, expenses) {
    budgetChart.data.datasets[0].data = [income, expenses];
    budgetChart.update();
}

// ====== Authentication ======
function showRegister() {
    loginSection.classList.add('hidden');
    registerSection.classList.remove('hidden');
}

function showLogin() {
    registerSection.classList.add('hidden');
    loginSection.classList.remove('hidden');
}

function handleRegister(e) {
    e.preventDefault();
    const name = document.getElementById('register-name').value.trim();
    const email = document.getElementById('register-email').value.trim();
    const password = document.getElementById('register-password').value;

    if (!name || !email || !password) {
        alert("Please fill in all fields.");
        return;
    }

    currentUser = {
        name,
        email,
        password,
        transactions: [],
        categories: [],
        savingsGoals: []
    };
    initializeApp();
}

function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;

    currentUser = {
        name: email.split('@')[0],
        email,
        password,
        transactions: [],
        categories: [],
        savingsGoals: []
    };
    initializeApp();
}

function handleLogout() {
    currentUser = null;
    appSection.classList.add('hidden');
    loginSection.classList.remove('hidden');
}

// ====== App Initialization ======
function initializeApp() {
    loginSection.classList.add('hidden');
    registerSection.classList.add('hidden');
    appSection.classList.remove('hidden');
    userName.textContent = currentUser.name;

    if (!budgetChart) {
        initializeChart();
    }
    updateCategoryDropdown();
    displayTransactions();
    displaySavingsGoals();
}

function displayTransactions() {
    transactionList.innerHTML = "";
    let incomeTotal = 0;
    let expenseTotal = 0;

    currentUser.transactions.forEach(tx => {
        const li = document.createElement('li');
        li.classList.add(tx.type);
        li.innerHTML = `
            <span>${tx.date}: ${tx.description} (${tx.category})</span>
            <span>KSh${tx.amount.toFixed(2)}</span>
        `;

        if (tx.type === "income") {
            incomeTotal += tx.amount;
        } else {
            expenseTotal += tx.amount;
        }

        transactionList.appendChild(li);
    });

    incomeAmount.textContent = KSh${incomeTotal.toFixed(2)};
    expenseAmount.textContent = KSh${expenseTotal.toFixed(2)};
    balanceAmount.textContent = KSh${(incomeTotal - expenseTotal).toFixed(2)};
    updateChart(incomeTotal, expenseTotal);
}

// ====== Category Functions ======
function updateCategoryDropdown() {
    categorySelect.innerHTML = '<option value="">Select Category</option>';
    currentUser.categories.forEach(cat => {
        const option = document.createElement('option');
        option.value = cat;
        option.textContent = cat;
        categorySelect.appendChild(option);
    });
}

addCategoryBtn.addEventListener('click', () => {
    const newCategory = prompt("Enter a new category name:");
    if (newCategory && newCategory.trim() !== "") {
        const trimmedCategory = newCategory.trim();
        if (currentUser.categories.includes(trimmedCategory)) {
            alert(Category "${trimmedCategory}" already exists.);
        } else {
            currentUser.categories.push(trimmedCategory);
            updateCategoryDropdown();
            alert(Category "${trimmedCategory}" added successfully!);
        }
    }
});

// ====== Savings Goals ======
function displaySavingsGoals() {
    savingsGoalsContainer.innerHTML = "";

    currentUser.savingsGoals.forEach((goal, index) => {
        const goalDiv = document.createElement('div');
        goalDiv.classList.add('savings-goal');
        goalDiv.innerHTML = `
            <h4>${goal.name}</h4>
            <p>Target: KSh${goal.target.toFixed(2)}</p>
            <p>Saved: KSh${goal.saved.toFixed(2)}</p>
            <button onclick="addToSavings(${index})">Add to Savings</button>
        `;
        savingsGoalsContainer.appendChild(goalDiv);
    });
}

function addToSavings(index) {
    const amount = parseFloat(prompt(Enter amount to add to "${currentUser.savingsGoals[index].name}":));
    if (isNaN(amount) || amount <= 0) {
        alert("Please enter a valid positive amount.");
        return;
    }

    currentUser.savingsGoals[index].saved += amount;

    if (currentUser.savingsGoals[index].saved >= currentUser.savingsGoals[index].target) {
        alert(🎉 Goal "${currentUser.savingsGoals[index].name}" reached!);
    }

    displaySavingsGoals();
}

addGoalBtn.addEventListener('click', () => {
    const goalName = prompt("Enter savings goal name:");
    const targetAmount = parseFloat(prompt("Enter target amount (KSh):"));

    if (!goalName || isNaN(targetAmount) || targetAmount <= 0) {
        alert("Invalid input. Please enter a valid name and positive amount.");
        return;
    }

    const newGoal = {
        name: goalName.trim(),
        target: targetAmount,
        saved: 0
    };

    currentUser.savingsGoals.push(newGoal);
    displaySavingsGoals();
    alert(Savings goal "${newGoal.name}" added successfully!);
});

// ====== Transaction Form ======
transactionForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const description = document.getElementById('description').value.trim();
    const category = categorySelect.value;
    const amount = parseFloat(document.getElementById('amount').value);
    const date = document.getElementById('date').value;
    const type = document.getElementById('type').value;
    const recurrence = document.getElementById('recurrence').value;

    if (!description || !category || isNaN(amount) || !date || !type) {
        alert("Please fill all fields correctly.");
        return;
    }

    const transaction = { description, category, amount, date, type, recurrence };
    currentUser.transactions.push(transaction);
    displayTransactions();
    transactionForm.reset();
});

// ====== Event Listeners ======
showRegisterLink.addEventListener('click', showRegister);
showLoginLink.addEventListener('click', showLogin);
loginForm.addEventListener('submit', handleLogin);
registerForm.addEventListener('submit', handleRegister);
logoutBtn.addEventListener('click', handleLogout);