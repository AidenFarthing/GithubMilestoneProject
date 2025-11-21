# GitHub Repository Management Test Framework  
**Sparta Global – Group Project (GraphQL Queries & Mutations + REST Assured)**

This project is an automated API testing framework designed to validate repository management functionality on GitHub using:

- **GitHub GraphQL API** (Queries & Mutations for Create, Read, Update operations)  
- **GitHub REST API** (Repository deletion)  
- **Cucumber with Gherkin feature files**  
- **Serenity BDD test runner**  
- **RestAssured for HTTP request execution**  
- **POJOs for structured response deserialization**

The framework includes both **successful (happy path)** and **unsuccessful (sad path)** scenarios, and is designed for easy reuse and extension.

## ⚙️ Setup & Configuration

Before running the tests, create a `config.properties` file in: src/test/resources/config.properties

Add the following values: 

github.token=YOUR_PERSONAL_ACCESS_TOKEN
repository.owner=YOUR_GITHUB_USERNAME
rest.url=https://api.github.com/
graphql.url=https://api.github.com/graphql

### 🔐 Token Requirements  
Your GitHub Personal Access Token must include:

- `repo`
- `delete_repo`

- ### Important: Use a Classic Personal Access Token
GitHub Fine-Grained tokens do not provide full GraphQL mutation support.  
You must use a **Classic Personal Access Token** with the following scopes:

- repo  
- delete_repo

**Do not commit this file.**  
It is ignored by `.gitignore` to prevent exposing credentials.

---

## 📁 Project Structure

The framework is organised into clear, modular components to support maintainability and scalability.

src/test/java/com/sparta/
- graphql/ → Base class for GraphQL requests  
- rest/ → Base class for REST requests  
- steps/ → All Cucumber step definitions  
- runners/ → Cucumber/Serenity test runners  
- pojos/ → POJO response models  

src/test/resources/
- features/ → All .feature files  
- graphql/ → GraphQL queries & mutation files  
Each responsibility is separated to support readable, reusable, and extensible test automation.

---
## 🖥️ Project Structure & IDE Notes

This is an **IntelliJ-based Java project**, structured to make API testing clear and modular.  
Simply clone the project, open it in IntelliJ, wait for Maven dependencies to download, and you're ready to run the tests.

### 📁 Key Folders

**src/test/java/com/sparta/**
- **steps/** → Step Definitions (GraphQL + REST)  
- **runners/** → Cucumber/Serenity Test Runners  
- **utils/** → Utility classes (Config loader, REST client)  
- **graphql/** → GraphQL base classes  
- **rest/** → REST base classes  
- **pojos/** → Response model classes  

**src/test/resources/**
- **features/** → Cucumber `.feature` files  
- **graphql/** → GraphQL Queries & Mutations (`.graphql` files)
  
## 📄 GraphQL Queries & Mutations

All GraphQL files used in the framework are stored in: src/test/resources/graphql/

This includes:

- `CreateRepo.graphql` (mutation)  
- `ReadRepo.graphql` (query)  
- `UpdateRepository.graphql` (mutation)

These files are read at runtime from the step definitions, allowing you to:

- Maintain clean separation of code and query  
- Easily update a GraphQL mutation without touching Java code  
- Quickly add new scenarios by creating new `.graphql` files  

---

## Class Diagram 
<img width="2653" height="1795" alt="image" src="https://github.com/user-attachments/assets/97a060ae-6554-4cb0-89db-d7bde1595d99" />



## ▶️ Running the Tests

You can run the test framework in two ways:

---

### **1️⃣ Run Individual Feature Tests (Using Test Runners)**

Each operation has its own test runner located in: src/test/java/com/sparta/runners/

Run them by opening the file and clicking the green ▶️ icon:

- `CreateRepoTestRunner` – runs Create Repository tests  
- `ReadRepoTestRunner` – runs Read Repository tests  
- `UpdateRepoTestRunner` – runs Update Repository tests  
- `DeleteRepoTestRunner` – runs Delete Repository tests  

---

### **2️⃣ Run ALL Tests Together**

Use the main runner:

- `TestRunner` – executes **all feature files** in one go

## 🤝 How Others Can Contribute

If another team wants to continue developing this project, they can follow these simple steps:

### 1️⃣ Add New Features
- Create new `.feature` files in `src/test/resources/features/`
- Write matching step definitions in `src/test/java/com/sparta/steps/`
- Add any required GraphQL queries/mutations in `src/test/resources/graphql/`

### 2️⃣ Extend Test Coverage
- Add new scenarios to existing feature files  
- Follow the current patterns:  
  - Temporary repo created in `@Before`  
  - Deleted in `@After`  
  - Config loaded from `config.properties`

### 3️⃣ Add or Update POJOs
If new fields are added in GitHub responses, update or create POJOs in: src/test/java/com/sparta/pojos/

### 4️⃣ Maintain Code Quality
- Use the existing package structure  
- Keep logic inside the Base classes for REST/GraphQL  
- Use pull requests for all changes

### 5️⃣ Git Workflow
- Create branches using: `feature/<your-name-or-task>`  
- Open a PR into the `dev` branch  
- Ensure all tests pass before merging

This makes it easy for any future group to expand, maintain, or improve the framework.

## ✅ Summary

This framework provides a complete, modular setup for testing GitHub repository functionality using both **GraphQL** and **REST API** approaches.  
It is fully reusable, easy to configure, and follows industry-standard patterns:

- Feature files for behaviour-driven design  
- Cucumber step definitions  
- POJO deserialization  
- Serenity runners  
- Clean separation of GraphQL/REST logic  
- Temporary test repositories created and cleaned up automatically  

Once your `config.properties` file is configured, all tests can be executed instantly through IntelliJ or the included test runners.

---


---

## 👥 Contributors

This framework was created collaboratively by:

- **Nahisah Nasleem**  
- **Khadijah Raja**  
- **Christopher Renwick**  
- **Aiden Farthing**

---
