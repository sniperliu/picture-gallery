-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, pass)
VALUES (:id, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

-- :name delete-user-images! :! :n
-- deletes all the images for the specified user
DELETE FROM files
WHERE owner = :owner

-- :name save-file! :! :n
-- save a file to the database
INSERT INTO files
(owner, type, name, data)
VALUES (:owner, :type, :name, :data)

-- :name list-thumbnails :? :*
-- :doc selects thumbnail names for the given gallery owner
SELECT owner, name FROM files
WHERE owner = :owner
  AND name LIKE 'thumb\_%'

-- :name get-image :? :1
-- :doc retrieve image data by name
SELECT type, data FROM files
WHERE name = :name
  AND owner = :owner

-- :name delete-file! :! :n
-- deletes the file with the given name and owner
DELETE FROM files
WHERE name = :name
 AND owner = :owner
