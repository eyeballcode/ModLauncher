rm -rf ~/eyeballcode.github.io/lib.mc/*

find . -type f -name "*.java" | grep -v "json" | xargs javadoc -d ~/eyeballcode.github.io/lib.mc/

PWD=`pwd`
cd ~/eyeballcode.github.io
git add lib.mc
git commit -m "Update jdoc"
git push
cd $PWD
xdg-open http://eyeballcode.github.io/lib.mc/
