export OAUTH2_PROXY_CLIENT_ID="6x34jFLguVr6Xu7vG9HIqEPGvv1gysQg"
export OAUTH2_PROXY_CLIENT_SECRET="2GQSq2Tsctniu4sbRGfGVQSkM9Wo3rT82sBWVRgY0_bagV7dIm-t0KmXOT70GlXx"
export OAUTH2_PROXY_COOKIE_SECRET="aO8xeed8dTEZu1g5kLoczg=="
# To set new cookie secret:
# export OAUTH2_PROXY_COOKIE_SECRET=$(python3 -c 'import os,base64; print(base64.b64encode(os.urandom(16)).decode("ascii"))')

# delete helm chart
microk8s helm3 delete couchpotato
# Install helm chart with debug
microk8s helm3 install --set oauth.clientId=$OAUTH2_PROXY_CLIENT_ID --set oauth.secret=$OAUTH2_PROXY_CLIENT_SECRET --set oauth.baseUrl=couchpotato.eu.auth0.com/   couchpotato .
