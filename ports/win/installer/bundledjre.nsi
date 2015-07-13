!define MULTIUSER_EXECUTIONLEVEL Highest
!define MULTIUSER_MUI
!define MULTIUSER_INSTALLMODE_COMMANDLINE
!include MultiUser.nsh
!include MUI2.nsh

;--------------------------------
;General

  ;Name and file
  Name "Docubricks"
  OutFile "install.exe"

  ;Default installation folder
  InstallDir "$LOCALAPPDATA\Docubricks"

  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\Docubricks" ""

  ;Request application privileges for Windows Vista
  RequestExecutionLevel user


;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "license.txt"
;  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MULTIUSER_PAGE_INSTALLMODE
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH


;--------------------------------
;Languages

  !insertmacro MUI_LANGUAGE "English"



;-----------------------------------------------------------------------------------
;Installer Sections
Section "Dummy Section" SecDummy

  SetOutPath "$INSTDIR"

  ;ADD YOUR OWN FILES HERE...

;  CreateDirectory $INSTDIR
;  CopyFiles files\*.* $INSTDIR

  File /r "docubricks\*.*"  
  IfErrors FileError
  File /r "jre\*.*"  
  IfErrors FileError
  
  CreateShortCut "$SMPROGRAMS\Docubricks.lnk" "$INSTDIR\start.exe"
  
  ;Store installation folder
  WriteRegStr HKCU "Software\Docubricks" "" $INSTDIR

  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"

Goto FileEnd

  FileError:
  DetailPrint "Could not install some files"
  SetDetailsView show
  MessageBox MB_OK|MB_ICONSTOP|MB_TOPMOST "Could not install some files"
  Abort
  FileEnd:

SectionEnd

;-----------------------------------------------------------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "A test section."

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;-----------------------------------------------------------------------------------
;Uninstaller Section

Section "Uninstall"


;  Delete "$INSTDIR\labstory"
  Delete "$INSTDIR\*.*"

;  Delete "$INSTDIR\Uninstall.exe"

  RMDir "$INSTDIR"

  DeleteRegKey /ifempty HKCU "Software\Docubricks"

SectionEnd
