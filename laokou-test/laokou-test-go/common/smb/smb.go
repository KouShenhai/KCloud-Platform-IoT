package smb

import (
	"github.com/hirochachacha/go-smb2"
	"log"
	"net"
	"regexp"
)

type Smb struct {
	Network   string
	Host      net.IP
	Port      int
	Username  string
	Password  string
	ShareName string
}

type Client struct {
	conn    net.Conn
	dialer  *smb2.Dialer
	session *smb2.Session
	share   *smb2.Share
}

func CloseSmb(c *Client) {
	err := c.share.Umount()
	if err != nil {
		return
	}
	err = c.session.Logoff()
	if err != nil {
		return
	}
	err = c.conn.Close()
	if err != nil {
		return
	}
}

func ReadAll(c *Client, path string) string {
	f, err := c.share.ReadFile(path)
	if err != nil {
		log.Printf("Read file failed：%s", err.Error())
		return ""
	}
	return string(f)
}

func Search(c *Client, basePath string, pattern string) []string {
	var results []string
	regex := regexp.MustCompile(pattern)
	fis, err := c.share.ReadDir(basePath)
	if err != nil {
		log.Printf("Read file failed：%s", err.Error())
		return results
	}
	for _, fi := range fis {
		path := basePath + "/" + fi.Name()
		if fi.IsDir() {
			temp := Search(c, path, pattern)
			results = append(results, temp...)
		}
		if !fi.IsDir() && regex.MatchString(fi.Name()) {
			results = append(results, path)
		}
	}
	return results
}

func ConnectSmb(smb Smb) *Client {
	conn, err := net.DialTCP(smb.Network, nil, &net.TCPAddr{
		IP:   smb.Host,
		Port: smb.Port,
	})
	if err != nil {
		log.Printf("Tcp connect failed：%s", err.Error())
		return nil
	}
	d := &smb2.Dialer{
		Initiator: &smb2.NTLMInitiator{
			User:     smb.Username,
			Password: smb.Password,
		},
	}
	session, err := d.Dial(conn)
	if err != nil {
		log.Printf("Dial connect failed：%s", err.Error())
		return nil
	}
	share, err := session.Mount(smb.ShareName)
	if err != nil {
		log.Printf("Share failed：%s", err.Error())
	}
	return &Client{
		conn:    conn,
		dialer:  d,
		session: session,
		share:   share,
	}
}
