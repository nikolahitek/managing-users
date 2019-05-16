INSERT INTO users (username, firstname, lastname, email, password, activated, role, dtype) VALUES
  ('user1', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 0, 'Account'),
  ('user2', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 0, 'Account'),
  ('user3', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 0, 'Account');

INSERT INTO departments (ID, name) VALUES
  (1, 'DPT_001'),
  (2, 'DPT_002'),
  (3, 'DPT_003');

INSERT INTO users (username, firstname, lastname, email, password, activated, role, dep_id, dtype) VALUES
  ('employee1', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 1, 1, 'Employee'),
  ('employee2', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 1, 1, 'Employee'),
  ('employee3', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 1, 2, 'Employee'),
  ('manager1', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 2, 1, 'Manager'),
  ('manager2', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 2, 2, 'Manager'),
  ('manager3', 'Name', 'Surname', 'demo@gmail.com', '$2a$10$QkwB8tfLia1o5/cFu1cFM.C7ojQaBaBkUxDtQB4KQxilMDiVcLGf.', true, 2, 3, 'Manager');