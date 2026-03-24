<h1 align="center">
  <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M12 3C10.3431 3 9 4.34315 9 6V11C9 12.6569 10.3431 14 12 14C13.6569 14 15 12.6569 15 11V6C15 4.34315 13.6569 3 12 3Z" stroke="currentColor" stroke-width="1.8"/>
    <path d="M6 10.5V11C6 14.3137 8.68629 17 12 17C15.3137 17 18 14.3137 18 11V10.5" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M12 17V21" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M9 21H15" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
  </svg>
  Voice Assistant Desktop
</h1>

<p align="center">
  A JavaFX desktop application designed to improve digital accessibility for users with visual impairments
  or limited hand mobility through voice-based interaction.
</p>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M12 8V13L15 16" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
    <circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.8"/>
  </svg>
  Project Overview
</h2>

<p>
  <strong>Voice Assistant Desktop</strong> is a Java-based desktop application that helps users interact with
  their computer using <strong>voice commands</strong> instead of a keyboard or mouse.
</p>

<p>
  The system captures the user’s voice, converts it into text, analyzes the command using predefined rules,
  and executes authorized actions such as <strong>creating, reading, updating, and deleting files</strong>.
  It also provides <strong>voice feedback</strong> to ensure a complete and accessible interaction.
</p>

<p>
  The project is structured with a clear separation between:
</p>

<ul>
  <li><strong>Front Office</strong> – JavaFX user interface</li>
  <li><strong>Back Office</strong> – business logic, CRUD management, and administration</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M12 3L19 7V12C19 16.4183 15.4183 20 11 20H5V15C5 8.37258 8.37258 3 12 3Z" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/>
    <path d="M9 11H15" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M12 8V14" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
  </svg>
  Objectives
</h2>

<ul>
  <li>Provide an accessible desktop solution for people with disabilities</li>
  <li>Enable hands-free interaction through voice commands</li>
  <li>Implement a clean JavaFX desktop architecture</li>
  <li>Build CRUD functionality with relational entities</li>
  <li>Apply server-side validation and good software structure</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <rect x="3" y="4" width="7" height="7" rx="1.5" stroke="currentColor" stroke-width="1.8"/>
    <rect x="14" y="4" width="7" height="7" rx="1.5" stroke="currentColor" stroke-width="1.8"/>
    <rect x="3" y="14" width="7" height="7" rx="1.5" stroke="currentColor" stroke-width="1.8"/>
    <rect x="14" y="14" width="7" height="7" rx="1.5" stroke="currentColor" stroke-width="1.8"/>
  </svg>
  Architecture
</h2>

<ul>
  <li><strong>Front-End:</strong> JavaFX + Scene Builder</li>
  <li><strong>Back-End:</strong> Java services and business logic</li>
  <li><strong>Database:</strong> MySQL with JDBC</li>
  <li><strong>IDE:</strong> Eclipse</li>
  <li><strong>Build Tool:</strong> Maven</li>
  <li><strong>Version Control:</strong> GitHub</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M4 6H20" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M4 12H20" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M4 18H14" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
  </svg>
  Project Structure
</h2>

<pre><code>src/main/java
│
├── app                     # Application entry point
├── controllers.front       # Front Office controllers
├── controllers.back        # Back Office controllers
├── tn.esprit.entities      # Entity classes
├── tn.esprit.models        # TableView row models
├── tn.esprit.services      # CRUD and business logic
├── tn.esprit.config        # DB connection and session management
│
src/main/resources
├── front                   # Front Office FXML files
├── back                    # Back Office FXML files
├── styles                  # CSS files
</code></pre>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M5 12L10 17L19 8" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
  </svg>
  Implemented Features
</h2>

<h3>Authentication</h3>
<ul>
  <li>User registration</li>
  <li>User login</li>
  <li>Role-based access (Admin / User)</li>
  <li>Session management</li>
</ul>

<h3>User Management</h3>
<ul>
  <li>Add user</li>
  <li>Update user</li>
  <li>Delete user</li>
  <li>Display users</li>
  <li>Search users</li>
  <li>Email uniqueness validation</li>
</ul>

<h3>Role Management</h3>
<ul>
  <li>Add role</li>
  <li>Update role</li>
  <li>Delete role</li>
  <li>Prevent deletion of roles linked to users</li>
  <li>Search roles</li>
</ul>

<h3>Advanced Features</h3>
<ul>
  <li>Search and filtering</li>
  <li>Server-side validation in Java</li>
  <li>Protection against deleting the last admin</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M7 7H17V17H7V7Z" stroke="currentColor" stroke-width="1.8"/>
    <path d="M3 7H7" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M17 7H21" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M3 17H7" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M17 17H21" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
  </svg>
  Entities
</h2>

<p>Current entities:</p>
<ul>
  <li><strong>User</strong></li>
  <li><strong>Role</strong></li>
</ul>

<p>Planned entities:</p>
<ul>
  <li><strong>VoiceCommand</strong></li>
  <li><strong>CommandHistory</strong></li>
  <li><strong>ManagedFile</strong></li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <circle cx="6" cy="12" r="2" stroke="currentColor" stroke-width="1.8"/>
    <circle cx="18" cy="6" r="2" stroke="currentColor" stroke-width="1.8"/>
    <circle cx="18" cy="18" r="2" stroke="currentColor" stroke-width="1.8"/>
    <path d="M8 11L16 7" stroke="currentColor" stroke-width="1.8"/>
    <path d="M8 13L16 17" stroke="currentColor" stroke-width="1.8"/>
  </svg>
  Relationships
</h2>

<ul>
  <li>One <strong>Role</strong> can be assigned to many <strong>Users</strong></li>
  <li>Each <strong>User</strong> belongs to one <strong>Role</strong></li>
  <li>Planned: one <strong>User</strong> can have many <strong>CommandHistory</strong> records</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M12 3V21" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M3 12H21" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.8"/>
  </svg>
  Technologies Used
</h2>

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-17-orange">
  <img alt="JavaFX" src="https://img.shields.io/badge/JavaFX-UI-blue">
  <img alt="MySQL" src="https://img.shields.io/badge/MySQL-Database-blue">
  <img alt="JDBC" src="https://img.shields.io/badge/JDBC-Connector-green">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-Build-red">
  <img alt="Eclipse" src="https://img.shields.io/badge/Eclipse-IDE-purple">
  <img alt="GitHub" src="https://img.shields.io/badge/GitHub-Version%20Control-black">
</p>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M5 12H19" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M12 5L19 12L12 19" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
  </svg>
  How to Run the Project
</h2>

<h3>1. Clone the repository</h3>
<pre><code>git clone https://github.com/rawendhii/VoiceAssistantDesk.git</code></pre>

<h3>2. Create the database</h3>
<p>Create a MySQL database named:</p>
<pre><code>voice_assistant</code></pre>

<h3>3. Configure database access</h3>
<p>You can keep the default connection or override it using JVM arguments:</p>
<pre><code>-Ddb.user=root
-Ddb.password=1234
-Ddb.url=jdbc:mysql://127.0.0.1:3306/voice_assistant</code></pre>

<h3>4. Run the application</h3>
<p>Launch <strong>MainApp.java</strong> from Eclipse.</p>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M9 11L11 13L15 9" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
    <rect x="4" y="4" width="16" height="16" rx="2" stroke="currentColor" stroke-width="1.8"/>
  </svg>
  Validation and Testing
</h2>

<ul>
  <li>Manual CRUD testing completed</li>
  <li>Validation handled in Java controllers/services</li>
  <li>Unit tests for CRUD will be added next</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M12 20V10" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
    <path d="M8 14L12 10L16 14" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
    <path d="M5 4H19" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
  </svg>
  Future Improvements
</h2>

<ul>
  <li>Speech-to-Text integration</li>
  <li>Text-to-Speech feedback</li>
  <li>Voice command management</li>
  <li>Command history tracking</li>
  <li>File management integration</li>
  <li>AI-based command interpretation</li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M12 12C14.2091 12 16 10.2091 16 8C16 5.79086 14.2091 4 12 4C9.79086 4 8 5.79086 8 8C8 10.2091 9.79086 12 12 12Z" stroke="currentColor" stroke-width="1.8"/>
    <path d="M5 20C5 16.6863 8.13401 14 12 14C15.866 14 19 16.6863 19 20" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
  </svg>
  Authors
</h2>

<ul>
  <li>Rawen Dhibi</li>
  <li>Teammate Name</li>
  <li><strong>ESPRIm</strong></li>
</ul>

<hr>

<h2>
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
    <path d="M8 7V5C8 3.89543 8.89543 3 10 3H14C15.1046 3 16 3.89543 16 5V7" stroke="currentColor" stroke-width="1.8"/>
    <rect x="5" y="7" width="14" height="14" rx="2" stroke="currentColor" stroke-width="1.8"/>
  </svg>
  License
</h2>

<p>This project is developed for academic purposes.</p>