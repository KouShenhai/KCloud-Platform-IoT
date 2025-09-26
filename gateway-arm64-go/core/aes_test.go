package core

import "testing"

func TestAes(t *testing.T) {
	key := []byte("1234567890123456")
	data := []byte("hello world")
	encrypted, err := Encrypt(key, data)
	if err != nil {
		t.Error(err)
		return
	}
	decrypted, err := Decrypt(key, encrypted)
	if err != nil {
		t.Error(err)
		return
	}
	t.Log(string(decrypted))
}
