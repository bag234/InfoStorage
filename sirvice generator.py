user = "jenkins"
jenk_dir_st = "/home/{user}"
spring_cfg_st = "--spring.config.import=file:"

name = input("Service name: ")
file = open(f"java-{name}.service", 'w')

desc = input("Description(or empty): ")
if len(desc) < 1:
    desc = f"Simple service {name}"

file.write(f"""
[Unit]
Description={desc}
After=syslog.target network.target

""")

def generate_service_jenkins():
    print("[Service script generator]")
    dir = input("Working dir: ")
    dir = f"{jenk_dir_st.format(user=user)}/{dir}"
    jr_st = input("Name .jar to start: ")
    conf = input("Add Spring config [y,N]: ")
    pt_file = ""
    if conf.lower() == 'y':
        b = input("Path to file conf: ")
        pt_file = f"{spring_cfg_st}{b}"
    ad_els = input("Additional flag(or empty): ")
    file.write(f"""
[Service]
Type=simple
User={user}
WorkingDirectory={dir}
ExecStart=/usr/bin/java -jar {dir}/{jr_st} {pt_file} {ad_els}

Restart=on-failure
RestartSec=10

LimitNOFILE=65536
LimitNPROC=4096

StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=java-{name}

""")

if input("is jenkins service [y/N]: ").lower() == "y":
    generate_service_jenkins()

file.write("""
[Install]
WantedBy=multi-user.target
""")
file.flush()
file.close()